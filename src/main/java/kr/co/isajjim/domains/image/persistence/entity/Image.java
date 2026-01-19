package kr.co.isajjim.domains.image.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private AIStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;

    @Builder
    private Image(
            String imageUrl
    ) {
        this.imageUrl = imageUrl;
        this.status = AIStatus.PENDING;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
        if (!estimate.getImages().contains(this)) {
            estimate.getImages().add(this);
        }
    }
}
