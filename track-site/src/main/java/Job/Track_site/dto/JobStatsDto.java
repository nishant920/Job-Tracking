package Job.Track_site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobStatsDto {
    private long total;
    private long applied;
    private long interviewed;
    private long rejected;
    private long offer;
    private long noResponse;
}
