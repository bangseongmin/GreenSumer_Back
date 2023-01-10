package org.swyg.greensumer.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.swyg.greensumer.domain.ProductEntity;
import org.swyg.greensumer.domain.StoreEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.dto.Product;
import org.swyg.greensumer.dto.Store;
import org.swyg.greensumer.repository.ProductEntityRepository;
import org.swyg.greensumer.repository.StoreEntityRepository;
import org.swyg.greensumer.repository.UserEntityRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.swyg.greensumer.fixture.Fixtures.*;
import static org.swyg.greensumer.fixture.RequestFixture.*;

@DisplayName("비즈니스 로직 - 제로웨이스트 샵")
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService sut;

    @Mock private StoreEntityRepository storeEntityRepository;
    @Mock private UserEntityRepository userEntityRepository;
    @Mock private ProductEntityRepository productEntityRepository;

    private static UserEntity userEntity;
    private static StoreEntity storeEntity;
    private static ProductEntity productEntity;

    @BeforeAll
    static void setUp() {
        userEntity = getUserEntity();
        storeEntity = getStoreEntity();
        productEntity = getProductEntity();
    }

    @DisplayName("가게 정보를 입력하면, 가게를 생성한다.")
    @Test
    void givenStoreInfo_whenRequestingSaveStore_thenReturnNothing() {
        // Given
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findByName(any())).willReturn(Optional.empty());
        given(storeEntityRepository.save(any())).willReturn(storeEntity);

        // When
        Store store = sut.create(getStoreCreateRequest(), userEntity.getUsername());

        //Then
        assertThat(store)
                .hasFieldOrPropertyWithValue("name", getStoreCreateRequest().getName())
                .hasFieldOrPropertyWithValue("description", getStoreCreateRequest().getDescription())
                .hasFieldOrPropertyWithValue("address", getStoreCreateRequest().getAddress())
                .hasFieldOrPropertyWithValue("hours", getStoreCreateRequest().getHours())
                .hasFieldOrPropertyWithValue("lat", getStoreCreateRequest().getLat())
                .hasFieldOrPropertyWithValue("lng", getStoreCreateRequest().getLng())
                .hasFieldOrPropertyWithValue("logo", getStoreCreateRequest().getLogo());

        verify(userEntityRepository).findByUsername(any());
        verify(storeEntityRepository).findByName(any());
        verify(storeEntityRepository).save(any());
    }

    @DisplayName("수정된 가게 정보를 입력하면, 가게정보가 수정된다.")
    @Test
    void givenUpdatedStoreInfo_whenRequestingModifyStore_thenReturnStore() {
        // Given
        given(userEntityRepository.findByUsername(userEntity.getUsername())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findById(storeEntity.getId())).willReturn(Optional.of(storeEntity));
        given(storeEntityRepository.saveAndFlush(storeEntity)).willReturn(storeEntity);

        // When
        Store modifiedStore = sut.modify(getId(), getStoreModifyRequest(), getUsername());

        //Then
        assertThat(modifiedStore)
                .hasFieldOrPropertyWithValue("name", getStoreModifyRequest().getName())
                .hasFieldOrPropertyWithValue("description", getStoreModifyRequest().getDescription())
                .hasFieldOrPropertyWithValue("address", getStoreModifyRequest().getAddress())
                .hasFieldOrPropertyWithValue("hours", getStoreModifyRequest().getHours())
                .hasFieldOrPropertyWithValue("lat", getStoreModifyRequest().getLat())
                .hasFieldOrPropertyWithValue("lng", getStoreModifyRequest().getLng())
                .hasFieldOrPropertyWithValue("logo", getStoreModifyRequest().getLogo());

        verify(userEntityRepository, times(1)).findByUsername(getUsername());
        verify(storeEntityRepository, times(1)).findById(getId());
        verify(storeEntityRepository, times(1)).saveAndFlush(storeEntity);
    }

    @DisplayName("가게아이디를 입력하면 가게를 삭제한다.")
    @Test
    void givenStoreId_whenRequestingDeleteStore_thenReturnNothing() {
        // Given
        given(userEntityRepository.findByUsername(userEntity.getUsername())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findById(storeEntity.getId())).willReturn(Optional.of(storeEntity));
        doNothing().when(productEntityRepository).deleteAllByStore(storeEntity);
        doNothing().when(storeEntityRepository).deleteById(storeEntity.getId());

        // When
        sut.delete(storeEntity.getId(), userEntity.getUsername());

        //Then
        then(userEntityRepository).should().findByUsername(userEntity.getUsername());
        then(storeEntityRepository).should().findById(storeEntity.getId());
        then(productEntityRepository).should().deleteAllByStore(storeEntity);
        then(storeEntityRepository).should().deleteById(storeEntity.getId());
    }

    @DisplayName("모든 가게 리스트 조회")
    @Test
    void givenNothing_whenRequestingStoreList_thenReturnPageStore() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(userEntityRepository.findByUsername(userEntity.getUsername())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<Store> stores = sut.list(pageable, userEntity.getUsername());

        //Then
        assertThat(stores).isEmpty();
        then(userEntityRepository).should().findByUsername(userEntity.getUsername());
        then(storeEntityRepository).should().findAll(pageable);
    }

    @DisplayName("로그인한 계정에 본인 가게 리스트 조회")
    @Test
    void givenUser_whenRequestingStoreList_thenReturnPageStore() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(userEntityRepository.findByUsername(userEntity.getUsername())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findAllByUser(userEntity, pageable)).willReturn(Page.empty());

        // When
        Page<Store> stores = sut.mylist(pageable, userEntity.getUsername());

        //Then
        assertThat(stores).isEmpty();
        then(userEntityRepository).should().findByUsername(userEntity.getUsername());
        then(storeEntityRepository).should().findAllByUser(userEntity, pageable);
    }

    @DisplayName("가게에 제품 등록 요청하면 제품이 등록된다.")
    @Test
    void givenProduct_whenRequestingRegisterProduct_thenReturnProduct() {
        // Given
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findById(any())).willReturn(Optional.of(storeEntity));
        given(productEntityRepository.save(any())).willReturn(productEntity);

        // When
        Product product = sut.registerProduct(storeEntity.getId(), getProductCreateRequest(), userEntity.getUsername());

        //Then
        assertThat(product)
                .hasFieldOrPropertyWithValue("name", productEntity.getName())
                .hasFieldOrPropertyWithValue("description", productEntity.getDescription())
                .hasFieldOrPropertyWithValue("price", productEntity.getPrice())
                .hasFieldOrPropertyWithValue("stock", productEntity.getStock())
                .hasFieldOrPropertyWithValue("image", productEntity.getImage());

        then(userEntityRepository).should().findByUsername(any());
        then(storeEntityRepository).should().findById(any());
        then(productEntityRepository).should().save(any());
    }

    @DisplayName("제품 아이디입력하고 제품을 수정하면 해당 가게에 등록된 제품을 수정한다.")
    @Test
    void givenUpdatedProduct_whenRequestingModifyProduct_thenReturnProduct() {
        // Given
        ProductEntity modifiedProductEntity = ModifyProductEntity(productEntity);

        given(userEntityRepository.findByUsername(any())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findById(any())).willReturn(Optional.of(storeEntity));
        given(productEntityRepository.findById(any())).willReturn(Optional.of(productEntity));
        given(productEntityRepository.saveAndFlush(any())).willReturn(modifiedProductEntity);

        // When
        Product product = sut.modifyProduct(storeEntity.getId(), productEntity.getId(), getProductModifyRequest(), userEntity.getUsername());

        //Then
        assertThat(product)
                .hasFieldOrPropertyWithValue("name", getProductModifyRequest().getName())
                .hasFieldOrPropertyWithValue("description", getProductModifyRequest().getDescription())
                .hasFieldOrPropertyWithValue("price", getProductModifyRequest().getPrice())
                .hasFieldOrPropertyWithValue("stock", getProductModifyRequest().getStock())
                .hasFieldOrPropertyWithValue("image", getProductModifyRequest().getImage());

        verify(userEntityRepository, times(1)).findByUsername(any());
        verify(storeEntityRepository, times(1)).findById(any());
        verify(productEntityRepository, times(1)).findById(any());
        verify(productEntityRepository, times(1)).saveAndFlush(any());
    }

    @DisplayName("제품 아이디입력하면 해당 가게에 등록된 제품을 삭제한다.")
    @Test
    void givenProductId_whenRequestingDeleteProduct_thenReturnNothing() {
        // Given
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.of(userEntity));
        given(storeEntityRepository.findById(any())).willReturn(Optional.of(storeEntity));
        doNothing().when(productEntityRepository).deleteById(any());

        // When
        sut.deleteProduct(storeEntity.getId(), productEntity.getId(), userEntity.getUsername());

        //Then
        verify(userEntityRepository, times(1)).findByUsername(any());
        verify(storeEntityRepository, times(1)).findById(any());
        verify(productEntityRepository, times(1)).deleteById(any());
    }

    @DisplayName("가게 아이디입력하면 해당 가게에 등록된 제품 리스트를 반환한다.")
    @Test
    void givenStoreId_whenRequestingProductList_thenReturnProductList() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(storeEntityRepository.findById(any())).willReturn(Optional.of(storeEntity));
        given(productEntityRepository.findAllByStore(storeEntity, pageable)).willReturn(Page.empty());

        // When
        Page<Product> products = sut.getProductList(storeEntity.getId(), pageable);

        //Then
        assertThat(products).isEmpty();
        verify(storeEntityRepository, times(1)).findById(any());
        verify(productEntityRepository, times(1)).findAllByStore(storeEntity, pageable);
    }

    @DisplayName("가게 아이디와 제품 아이디를 주면 해당 가게에 등록된 제품를 반환한다.")
    @Test
    void givenStoreIdAndProductId_whenRequestingProductOnStore_thenReturnProduct() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(storeEntityRepository.findById(any())).willReturn(Optional.of(storeEntity));
        given(productEntityRepository.findByStoreAndId(storeEntity, productEntity.getId())).willReturn(Optional.of(createProductEntity()));

        // When
        Product product = sut.getProduct(storeEntity.getId(), productEntity.getId());

        //Then
        verify(storeEntityRepository, times(1)).findById(any());
        verify(productEntityRepository, times(1)).findByStoreAndId(storeEntity, productEntity.getId());
    }

}