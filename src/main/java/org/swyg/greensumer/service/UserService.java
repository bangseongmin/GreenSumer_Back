package org.swyg.greensumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swyg.greensumer.domain.AddressEntity;
import org.swyg.greensumer.domain.SellerStoreEntity;
import org.swyg.greensumer.domain.StoreEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.domain.constant.UserRole;
import org.swyg.greensumer.dto.User;
import org.swyg.greensumer.dto.request.UpdateUserRequest;
import org.swyg.greensumer.dto.request.UserSignUpRequest;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.repository.SellerStoreEntityRepository;
import org.swyg.greensumer.repository.UserCacheRepository;
import org.swyg.greensumer.repository.UserEntityRepository;
import org.swyg.greensumer.utils.JwtTokenUtils;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final UserEntityRepositoryService userEntityRepositoryService;
    private final StoreService storeService;
    private final AddressService addressService;
    private final SellerStoreEntityRepository sellerStoreEntityRepository;
    private final UserCacheRepository userCacheRepository;
    private final VerificationService verificationService;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}") private String secretKey;
    @Value("${jwt.token.expired-time-ms}") private Long expiredTimeMs;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpiredTimeMs(Long expiredTimeMs) {
        this.expiredTimeMs = expiredTimeMs;
    }

    @Transactional
    public User signup(UserSignUpRequest request) {
        userEntityRepositoryService.existUsername(request.getUsername());

        AddressEntity addressEntity = null;

        if(request.getAddress() != null){
            addressEntity = addressService.findAddressEntity(request.getAddress(), request.getAddress(), request.getLat(), request.getLat());
        }

        UserEntity userEntity = UserEntity.of(
                request.getUsername(),
                encoder.encode(request.getPassword()),
                request.getNickname(),
                request.getEmail(),
                addressEntity
        );

        UserEntity savedUser = userEntityRepository.save(userEntity);

        if(savedUser.getRole() == UserRole.SELLER){
            mappingSellerAndStore(savedUser, addressEntity);
        }

        return User.fromEntity(savedUser);
    }

    private void mappingSellerAndStore(UserEntity userEntity, AddressEntity addressEntity) {
        StoreEntity storeEntity = storeService.searchStore(addressEntity);

        SellerStoreEntity sellerStoreEntity = SellerStoreEntity.of(storeEntity, userEntity);
        storeEntity.addSellerStore(sellerStoreEntity);
        sellerStoreEntityRepository.save(sellerStoreEntity);
    }

    public String login(String username, String password) {
        User user = userEntityRepositoryService.loadUserByUsername(username);

        // 비밀번호 체크
        if (!encoder.matches(password, user.getPassword())) {
            throw new GreenSumerBackApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        userCacheRepository.setUser(user);

        // 토큰 생성
        return JwtTokenUtils.generateToken(username, secretKey, expiredTimeMs);
    }

    @Transactional(readOnly = true)
    public void existUsername(String username) {
        userEntityRepositoryService.existUsername(username);
    }

    @Transactional
    public User findUsername(String email, String code) {
        verificationService.checkMail(email, code);

        UserEntity userEntity = userEntityRepositoryService.findByEmail(email);

        verificationService.clear(email);

        return User.fromEntity(userEntity);
    }

    @Transactional
    public void findPassword(String username, String email, String code, String password) {
        verificationService.checkMail(email, code);

        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);

        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new GreenSumerBackApplicationException(ErrorCode.SAME_AS_PREVIOUS_PASSWORD, String.format("%s same as before", password));
        }

        verificationService.clear(email);
        userEntity.setPassword(encoder.encode(password));
    }

    @Transactional
    public User updateUserInfo(UpdateUserRequest request, String username) {
        if (request.getUsername() != username) {
            throw new GreenSumerBackApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission to %s", username, request.getUsername()));
        }

        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);

        userEntity.setPassword(encoder.encode(request.getPassword()));
        userEntity.setNickname(request.getNickname());
        userEntity.setEmail(request.getEmail());

        if(userEntity.getRole() == UserRole.SELLER){
            AddressEntity addressEntity = addressService.updateAddress(userEntity.getAddressEntity().getId(), request.getAddress(), request.getAddress(), request.getLat(), request.getLat());
            userEntity.setAddressEntity(addressEntity);
        }

        return User.fromEntity(userEntity);
    }

}

