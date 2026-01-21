package kr.co.isajjim.domains.image.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Furniture> furnitures = new ArrayList<>();

    @Builder
    private Image(
            String imageUrl
    ) {
        this.imageUrl = imageUrl;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
        if (!estimate.getImages().contains(this)) {
            estimate.getImages().add(this);
        }
    }

    public void addFurniture(Furniture furniture) {
        this.furnitures.add(furniture);
        if (furniture.getImage() != this) {
            furniture.setImage(this);
        }
    }
}
