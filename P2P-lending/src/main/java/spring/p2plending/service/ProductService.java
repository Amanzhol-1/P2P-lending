package spring.p2plending.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import spring.p2plending.dto.ProductRequestDTO;
import spring.p2plending.dto.ProductResponseDTO;
import spring.p2plending.model.Product;
import spring.p2plending.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Ранее было findAll() + фильтрация:
     *   .stream().filter(Product::isAvailable)...
     * Теперь напрямую вызываем findByAvailableTrue().
     * Если в таблице 100 тысяч записей, а «available = true» только у 100, то мы получаем только эти 100
     * Это существенно повышает производительность и снижает потребление памяти
     */
    @Cacheable(value = "activeProducts")
    public List<ProductResponseDTO> getActiveProducts() {
        List<Product> products = productRepository.findByAvailableTrue();
        return products.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Дополнительно можно добавить @CacheEvict, чтобы сбрасывать кэш
     * 'activeProducts', если меняется состояние продукта.
     */
    @Transactional
    @CacheEvict(value = "activeProducts", allEntries = true)
    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .available(productRequest.getAvailable() != null ? productRequest.getAvailable() : true)
                .build();

        Product saved = productRepository.save(product);
        return mapToResponseDTO(saved);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .available(product.isAvailable())
                .build();
    }
}
