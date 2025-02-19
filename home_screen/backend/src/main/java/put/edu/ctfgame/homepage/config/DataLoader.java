package put.edu.ctfgame.homepage.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import put.edu.ctfgame.homepage.entity.Flag;
import put.edu.ctfgame.homepage.entity.Level;
import put.edu.ctfgame.homepage.entity.hint.Hint;
import put.edu.ctfgame.homepage.enums.LevelName;
import put.edu.ctfgame.homepage.repository.FlagRepository;
import put.edu.ctfgame.homepage.repository.HintRepository;
import put.edu.ctfgame.homepage.repository.LevelRepository;

import put.edu.ctfgame.homepage.util.MailJsonHandler;

import put.edu.ctfgame.homepage.entity.ServerInstance;
import put.edu.ctfgame.homepage.repository.ServerRepository;



import java.io.IOException;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final LevelRepository levelRepository;
    private final HintRepository hintRepository;

    private final FlagRepository flagRepository;
    private final MailJsonHandler mailJsonHandler;

    private final ServerRepository serverInstanceRepository;


    @Override
    public void run(String... args) throws IOException {
        initFlags();
        initLevels();
        initHints();

        initMails();

        initUuids();
    }

    private void initUuids() {
        try{
        BufferedReader reader = new BufferedReader(new FileReader("port-uuids.csv"));

            String line = "";          

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int backendPort = Integer.parseInt(data[0]);
                String id = data[1];
                
                serverInstanceRepository.save(ServerInstance.builder().id(id).backendPort(backendPort).frontendPort(0).username(null).isRunning(false).build());
            }     
        } catch (Exception e) {
                e.printStackTrace();
        }

    }

    private void initFlags() {
        var flags = List.of(
                Flag.builder()
                        .id(1L)
                        .flag("TGHM{4dmin5_c4n_m4k3_mu5ic_t00}")
                        .totalPoints(210L)
                        .build(),
                Flag.builder()
                        .id(2L)
                        .flag("TGHM{1_g0_v4_b4nqu3}")
                        .totalPoints(500L)
                        .build(),
                Flag.builder()
                        .id(3L)
                        .flag("TGHM{sig3egv_tr4ns4cti0n_f4ult}")
                        .totalPoints(300L)
                        .build(),
                Flag.builder()
                        .id(4L)
                        .flag("TGHM{m3m0ry_l34ks_th3_s4m3_as_y0ur_m0n3y}")
                        .totalPoints(250L)
                        .build(),
                Flag.builder()
                        .id(6L)
                        .flag("TGHM{ap0litical_h4cking_f0rum_lm4o}")
                        .totalPoints(150L)
                        .build(),
                Flag.builder()
                        .id(5L)
                        .flag("TGHM{admin_sh0uld_g0_0n_a_c00kie_di3t}")
                        .totalPoints(100L)
                        .build(),
                Flag.builder()
                        .id(7L)
                        .flag("TGHM{sc4mm3rs_g3t_sc4mm3d}")
                        .totalPoints(400L)
                        .build(),
                Flag.builder()
                        .id(8L)
                        .flag("TGHM{pr1v4t3_53rv3r_n0t_s0_pr1v4t3}")
                        .totalPoints(500L)
                        .build()
        );
        flagRepository.saveAll(flags);
    }

    private void initLevels() {
        var levels = List.of(
                Level.builder()
                        .name(LevelName.BANK)
                        .description("Use SQLin and brute force to get the flag")
                        .build(),
                Level.builder()
                        .name(LevelName.FORUM)
                        .description("Use cookie tampering and idor to get the flag")
                        .build(),
                Level.builder()
                        .name(LevelName.COMPANY)
                        .description("Use command injection to get the flag")
                        .build(),
                Level.builder()
                        .name(LevelName.MESSENGER)
                        .description("Use xss and csrf to get the flag")
                        .build()
        );
        levelRepository.saveAll(levels);
    }

    private void initHints() {
        // Initialize hints for Flag 1
        var flag1 = flagRepository.findById(1L).orElseThrow();
        var flag1Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("There's no POINT just idly awaiting the END. Touch grass to find that which is HIDDEN, my friend.")
                        .flag(flag1)
                        .pointsCost(56)
                        .ordinal(1)
                        .build(),
                Hint.builder()
                        .title("Hint 2")
                        .content("Not all streams of mischief have yet been perfected. Don't BASH in your skull but COMMAND and INJECT it.")
                        .flag(flag1)
                        .pointsCost(56)
                        .ordinal(2)
                        .build(),
                Hint.builder()
                        .title("Hint 3")
                        .content("The sharper the body the sharper the mind. One must not just divide, but conquer and FIND.")
                        .flag(flag1)
                        .pointsCost(56)
                        .ordinal(3)
                        .build()
        );
        hintRepository.saveAll(flag1Hints);

        // Initialize hints for Flag 2
        var flag2 = flagRepository.findById(2L).orElseThrow();
        var flag2Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("Shrek 2 was alright but I preferred the prequel... Don't INJECT your falsehoods, because all men love SQL.")
                        .flag(flag2)
                        .pointsCost(100)
                        .ordinal(1)
                        .build(),
                Hint.builder()
                        .title("Hint 2")
                        .content("What's the use pondering EXISTENCE? It's not like you'll use that knowledge to hack the system.")
                        .flag(flag2)
                        .pointsCost(100)
                        .ordinal(2)
                        .build(),
                Hint.builder()
                        .title("Hint 3")
                        .content("Let's build a house: First, gather a UNION of friends. Before NAMING your BASE, think of the number of COLUMNS needed. Place the TABLE next, and you're set!")
                        .flag(flag2)
                        .pointsCost(100)
                        .ordinal(3)
                        .build(),
                Hint.builder()
                        .title("Hint 4")
                        .content("The password is ATTACKED, so one must be wary. Defend it at all costs!! Use a ... DICTIONARY?")
                        .flag(flag2)
                        .pointsCost(100)
                        .ordinal(4)
                        .build()
        );
        hintRepository.saveAll(flag2Hints);

        // Initialize hints for Flag 3
        var flag3 = flagRepository.findById(3L).orElseThrow();
        var flag3Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("🕑 toc-tou... toc-tou... toc-tou")
                        .flag(flag3)
                        .pointsCost(120)
                        .ordinal(1)
                        .build(),
                Hint.builder()
                        .title("Hint 2")
                        .content("Please don't eat hastily, it's not a RACE. Never repeat it should you BURP in one's face!")
                        .flag(flag3)
                        .pointsCost(120)
                        .ordinal(2)
                        .build()
        );
        hintRepository.saveAll(flag3Hints);

        // Initialize hints for Flag 4
        var flag4 = flagRepository.findById(4L).orElseThrow();
        var flag4Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("A search for a UNION with winners, not losers. A savior SELECTED from all of the USERS!")
                        .flag(flag4)
                        .pointsCost(200)
                        .ordinal(1)
                        .build()
        );
        hintRepository.saveAll(flag4Hints);

        // Initialize hints for Flag 5
        var flag5 = flagRepository.findById(6L).orElseThrow();
        var flag5Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("A plant most magnificent starts with a bean. The most precious marvels avoid being seen...")
                        .flag(flag5)
                        .pointsCost(60)
                        .ordinal(1)
                        .build(),
                Hint.builder()
                        .title("Hint 2")
                        .content("My sincere apologies, I've never been this late! They asked me for my ID even though I'm 68!")
                        .flag(flag5)
                        .pointsCost(60)
                        .ordinal(2)
                        .build()
        );
        hintRepository.saveAll(flag5Hints);

        // Initialize hints for Flag 6
        var flag6 = flagRepository.findById(5L).orElseThrow();
        var flag6Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("There once was a dream team of no one but rookies. Not once did they work, for they only ate COOKIES.")
                        .flag(flag6)
                        .pointsCost(80)
                        .ordinal(1)
                        .build()
        );
        hintRepository.saveAll(flag6Hints);

        // Initialize hints for Flag 7
        var flag7 = flagRepository.findById(7L).orElseThrow();
        var flag7Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("- What should we call ourselves?\n- How about... Xenomorphic Super Squad\n- Yes! I Love it!!!")
                        .flag(flag7)
                        .pointsCost(80)
                        .ordinal(1)
                        .build(),
                Hint.builder()
                        .title("Hint 2")
                        .content("Hey hacker, I wanted to wish you luck, there are many ways to do that, even without the script TAG")
                        .flag(flag7)
                        .pointsCost(80)
                        .ordinal(2)
                        .build(),
                Hint.builder()
                        .title("Hint 3")
                        .content("We've done so many SESSIONS together that they've become a real TOKEN of our friendship.")
                        .flag(flag7)
                        .pointsCost(80)
                        .ordinal(3)
                        .build(),
                Hint.builder()
                        .title("Hint 4")
                        .content("It's strange how spiders don't have to use HOOKS to cling onto their WEBS")
                        .flag(flag7)
                        .pointsCost(80)
                        .ordinal(4)
                        .build()
        );
        hintRepository.saveAll(flag7Hints);

        // Initialize hints for Flag 8
        var flag8 = flagRepository.findById(8L).orElseThrow();
        var flag8Hints = List.of(
                Hint.builder()
                        .title("Hint 1")
                        .content("My ex Robert published all my private photos when we broke up... Maybe all Roberts are the same and cannot be trusted with classified information?")
                        .flag(flag8)
                        .pointsCost(200)
                        .ordinal(1)
                        .build(),
                Hint.builder()
                        .title("Hint 2")
                        .content("I think our Xenomorphic Super Squad needs a motto. How about... Chase SUCCESS, Reach Further!")
                        .flag(flag8)
                        .pointsCost(200)
                        .ordinal(2)
                        .build()
        );
        hintRepository.saveAll(flag8Hints);
    }


    private void initMails() throws IOException {
        mailJsonHandler.parseMailList("mails.json");
    }
}