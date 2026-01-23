package kr.co.isajjim.domains.furniture.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "furniture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Furniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furniture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Enumerated(EnumType.STRING)
    private FurnitureLabel label;

    @Enumerated(EnumType.STRING)
    private FurnitureType type;

    private Double width;

    private Double height;

    private Double depth;

    private Double volume;

    private Double ratioWidth;

    private Double ratioHeight;

    private Double ratioDepth;

    private Integer quantity;

    @Builder
    private Furniture(
        FurnitureLabel label,
        FurnitureType type,
        Double width,
        Double height,
        Double depth,
        Double volume,
        Double ratioWidth,
        Double ratioHeight,
        Double ratioDepth,
        Integer quantity
    ) {
        this.label = label;
        this.type = type;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.volume = volume;
        this.ratioWidth = ratioWidth;
        this.ratioHeight = ratioHeight;
        this.ratioDepth = ratioDepth;
        this.quantity = quantity;
    }

    public void setImage(Image image) {
        this.image = image;
        if (!image.getFurnitures().contains(this)) {
            image.getFurnitures().add(this);
        }
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void validateBelongsTo(Long estimateId) {
        if (!this.image.getEstimate().getId().equals(estimateId)) {
            throw new BaseException(ResponseCode.INVALID_FURNITURE_ESTIMATE_ASSOCIATION);
        }
    }
}
