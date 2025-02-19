package put.edu.ctfgame.company;

import put.edu.ctfgame.company.entity.Worker;

public class TestDataFactory {
    public static Worker sampleWorker() {
        return Worker.builder()
                .id(1L)
                .name("John Doe")
                .build();
    }
}
