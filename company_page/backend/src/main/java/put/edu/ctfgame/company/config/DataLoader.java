package put.edu.ctfgame.company.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import put.edu.ctfgame.company.entity.Worker;
import put.edu.ctfgame.company.repository.WorkerRepository;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final WorkerRepository workerRepository;

    @Override
    public void run(String... args) {
        var worker1 = Worker.builder()
                .name("Jacques Pommedeterre")
                .position("Chief and Co-founder of the label")
                .year("2014")
                .description("Also known as A3S, he co-founded and organized the label \"from the inside\". The face of the brand and a musician known not only in Segfaultland!")
                .build();

        var worker2 = Worker.builder()
                .name("Caspar Quatch")
                .position("Director of Recording Area and Co-founder of the label")
                .year("2014")
                .description("A well-known, respected, and tested music engineer whose mixes will move the strings of the hardest heart!")
                .build();

        var worker3 = Worker.builder()
                .name("Kanye Zapad")
                .position("Director of Secrets Area")
                .year("2024")
                .description("A mysterious figure known for uncovering hidden truths and secrets within the industry.")
                .build();

        workerRepository.save(worker1);
        workerRepository.save(worker2);
        workerRepository.save(worker3);
    }
}