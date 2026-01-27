package kr.co.isajjim.domains.estimate.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "estimate_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EstimateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    private Integer quantity;

    @Builder
    public EstimateItem(Estimate estimate, ItemCategory category, ItemType type, Integer quantity) {
        this.estimate = estimate;
        this.category = category;
        this.type = type;
        this.quantity = quantity;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
        if (!estimate.getEstimateItems().contains(this)) {
            estimate.getEstimateItems().add(this);
        }
    }
}
