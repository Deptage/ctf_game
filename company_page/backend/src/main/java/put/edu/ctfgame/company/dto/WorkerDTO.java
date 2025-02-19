package put.edu.ctfgame.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.company.entity.Worker;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WorkerDTO {
    private String name;
    private String position;
    private String year;
    private String description;

    public static WorkerDTO from(Worker worker) {
        return WorkerDTO.builder()
                .name(worker.getName())
                .position(worker.getPosition())
                .year(worker.getYear())
                .description(worker.getDescription())
                .build();
    }
}
