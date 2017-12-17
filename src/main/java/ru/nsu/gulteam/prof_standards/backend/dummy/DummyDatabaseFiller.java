package ru.nsu.gulteam.prof_standards.backend.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.*;
import ru.nsu.gulteam.prof_standards.backend.domain.type.AttestationForm;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;
import ru.nsu.gulteam.prof_standards.backend.service.TrajectoryService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DummyDatabaseFiller {
    private MainRepository mainRepository;
    private UserRepository userRepository;
    private TemplateCourseRepository templateCourseRepository;
    private SkillsRepository skillsRepository;
    private ProfessionalStandardRepository professionalStandardRepository;
    private LaborFunctionRepository laborFunctionRepository;
    private KnowledgeRepository knowledgeRepository;
    private GeneralizedLaborFunctionRepository generalizedLaborFunctionRepository;
    private CourseRepository courseRepository;
    private BasicEducationProgramRepository basicEducationProgramRepository;
    private DummyDatabaseFillerProperties dummyDatabaseFillerProperties;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DummyDatabaseFiller(DummyDatabaseFillerProperties dummyDatabaseFillerProperties,
                               UserRepository userRepository,
                               TemplateCourseRepository templateCourseRepository,
                               SkillsRepository skillsRepository,
                               ProfessionalStandardRepository professionalStandardRepository,
                               LaborFunctionRepository laborFunctionRepository,
                               KnowledgeRepository knowledgeRepository,
                               GeneralizedLaborFunctionRepository generalizedLaborFunctionRepository,
                               CourseRepository courseRepository,
                               BasicEducationProgramRepository basicEducationProgramRepository,
                               MainRepository mainRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.templateCourseRepository = templateCourseRepository;
        this.skillsRepository = skillsRepository;
        this.professionalStandardRepository = professionalStandardRepository;
        this.laborFunctionRepository = laborFunctionRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.generalizedLaborFunctionRepository = generalizedLaborFunctionRepository;
        this.courseRepository = courseRepository;
        this.basicEducationProgramRepository = basicEducationProgramRepository;
        this.mainRepository = mainRepository;
        this.passwordEncoder = passwordEncoder;
        this.dummyDatabaseFillerProperties = dummyDatabaseFillerProperties;
    }

    @PostConstruct
    public void fillDatabase() {
        if(!dummyDatabaseFillerProperties.needFill()){
            return;
        }

        mainRepository.clearAllRelations();
        mainRepository.clearAll();

        // Users
        List<User> users = Arrays.asList(
                new User("Kristina", "Popova", "kr111kr", passwordEncoder.encode("kr111kr")),
                new User("Kirill", "Batalin", "kir55rus", passwordEncoder.encode("kir55rus")),
                new User("Igor", "Ryzhakov", "gorod", passwordEncoder.encode("gorod")),
                new User("Nikita", "Mameyev", "asm_edf", passwordEncoder.encode("asm_edf"))
        );

        userRepository.save(users);

        // Professional standard
        ProfessionalStandard webDeveloper = professionalStandardRepository.save(new ProfessionalStandard("Web-developer", "A"));
        ProfessionalStandard microcontrollerDeveloper = professionalStandardRepository.save(new ProfessionalStandard("Microcontroller-developer", "B"));
        ProfessionalStandard sharpDeveloper = professionalStandardRepository.save(new ProfessionalStandard("C# developer", "C"));

        // Generalized labor function
        GeneralizedLaborFunction GW1 = generalizedLaborFunctionRepository.save(new GeneralizedLaborFunction("GW1", "GW1", 6));
        GeneralizedLaborFunction GW2 = generalizedLaborFunctionRepository.save(new GeneralizedLaborFunction("GW2", "GW2", 6));
        GeneralizedLaborFunction GW3 = generalizedLaborFunctionRepository.save(new GeneralizedLaborFunction("GW3", "GW3", 6));
        GW1 = generalizedLaborFunctionRepository.connectToProfessionalStandard(GW1, webDeveloper);
        GW2 = generalizedLaborFunctionRepository.connectToProfessionalStandard(GW2, webDeveloper);
        GW3 = generalizedLaborFunctionRepository.connectToProfessionalStandard(GW3, webDeveloper);

        GeneralizedLaborFunction GM1 = generalizedLaborFunctionRepository.save(new GeneralizedLaborFunction("GM1", "GM1", 7));
        GM1 = generalizedLaborFunctionRepository.connectToProfessionalStandard(GM1, microcontrollerDeveloper);

        GeneralizedLaborFunction GC1 = generalizedLaborFunctionRepository.save(new GeneralizedLaborFunction("GC1", "GC1", 8));
        GC1 = generalizedLaborFunctionRepository.connectToProfessionalStandard(GC1, sharpDeveloper);

        // Labor Function
        LaborFunction W1 = laborFunctionRepository.save(new LaborFunction("W1", "W1", 6));
        LaborFunction W2 = laborFunctionRepository.save(new LaborFunction("W2", "W2", 6));
        LaborFunction W3 = laborFunctionRepository.save(new LaborFunction("W3", "W3", 6));
        W1 = laborFunctionRepository.connectToGeneralizedLaborFunction(W1, GW1);
        W2 = laborFunctionRepository.connectToGeneralizedLaborFunction(W2, GW2);
        W3 = laborFunctionRepository.connectToGeneralizedLaborFunction(W3, GW3);

        LaborFunction M1 = laborFunctionRepository.save(new LaborFunction("M1", "M1", 7));
        M1 = laborFunctionRepository.connectToGeneralizedLaborFunction(M1, GM1);

        LaborFunction C1 = laborFunctionRepository.save(new LaborFunction("C1", "C1", 8));
        C1 = laborFunctionRepository.connectToGeneralizedLaborFunction(C1, GC1);

        // Knowledge
        Knowledge KW1 = knowledgeRepository.save(new Knowledge("Angular 2"));
        Knowledge KW2 = knowledgeRepository.save(new Knowledge("Parallelism"));
        Knowledge KW3 = knowledgeRepository.save(new Knowledge("TCP"));
        KW1 = knowledgeRepository.connectToLaborFunction(KW1, W1);
        KW2 = knowledgeRepository.connectToLaborFunction(KW2, W1);
        KW3 = knowledgeRepository.connectToLaborFunction(KW3, W2);

        Knowledge KM1 = knowledgeRepository.save(new Knowledge("Thermodynamics"));
        Knowledge KM2 = knowledgeRepository.save(new Knowledge("Electricity"));
        KM1 = knowledgeRepository.connectToLaborFunction(KM1, M1);
        KM2 = knowledgeRepository.connectToLaborFunction(KM2, M1);

        Knowledge KC1 = knowledgeRepository.save(new Knowledge("C#"));
        KC1 = knowledgeRepository.connectToLaborFunction(KC1, C1);

        // Skills
        Skills S1 = skillsRepository.save(new Skills("Write code"));
        Skills S2 = skillsRepository.save(new Skills("Debug code"));
        S1 = skillsRepository.connectToLaborFunction(S1, W1);
        S2 = skillsRepository.connectToLaborFunction(S2, W1);
        S1 = skillsRepository.connectToLaborFunction(S1, M1);
        S2 = skillsRepository.connectToLaborFunction(S2, M1);
        S1 = skillsRepository.connectToLaborFunction(S1, C1);
        S2 = skillsRepository.connectToLaborFunction(S2, C1);

        Skills SW1 = skillsRepository.save(new Skills("Construct full stack"));
        Skills SW2 = skillsRepository.save(new Skills("Create web"));
        SW1 = skillsRepository.connectToLaborFunction(SW1, W1);
        SW2 = skillsRepository.connectToLaborFunction(SW2, W3);

        Skills SM1 = skillsRepository.save(new Skills("Solve equations"));
        Skills SM2 = skillsRepository.save(new Skills("Solder boards"));
        SM1 = skillsRepository.connectToLaborFunction(SM1, M1);
        SM2 = skillsRepository.connectToLaborFunction(SM2, M1);

        Skills SC1 = skillsRepository.save(new Skills("Knock on the keyboard"));
        SC1 = skillsRepository.connectToLaborFunction(SC1, C1);

        // Basic Education Program
        BasicEducationProgram educationProgram = basicEducationProgramRepository.save(new BasicEducationProgram("Test education program"));

        // Courses
        Course programming = courseRepository.save(new Course(36, 1, AttestationForm.EXAM, "Programming C#"));
        Course physics = courseRepository.save(new Course(36, 1, AttestationForm.EXAM, "Thermodynamics"));
        Course english = courseRepository.save(new Course(36, 3, AttestationForm.EXAM, "English language"));
        programming = courseRepository.connectToProgram(programming, educationProgram);
        physics = courseRepository.connectToProgram(physics, educationProgram);
        english = courseRepository.connectToProgram(english, educationProgram);

        // Template courses
        TemplateCourse t2 = templateCourseRepository.save(new TemplateCourse(36, 2, AttestationForm.EXAM));
        TemplateCourse t3 = templateCourseRepository.save(new TemplateCourse(36, 3, AttestationForm.EXAM));
        t2 = templateCourseRepository.connectToProgram(t2, educationProgram);
        t3 = templateCourseRepository.connectToProgram(t3, educationProgram);

        // Courses, that based on templates
        Course createServer = courseRepository.save(new Course(36, 2, AttestationForm.EXAM, "Create web server"));
        Course writeWebApplication = courseRepository.save(new Course(36, 2, AttestationForm.EXAM, "Write web application"));
        Course createKeyboard = courseRepository.save(new Course(36, 2, AttestationForm.EXAM, "Create keyboard"));
        createServer = courseRepository.connectToTemplate(createServer, t2);
        createServer = courseRepository.connectToProgram(createServer, educationProgram);
        writeWebApplication = courseRepository.connectToTemplate(writeWebApplication, t2);
        writeWebApplication = courseRepository.connectToProgram(writeWebApplication, educationProgram);
        createKeyboard = courseRepository.connectToTemplate(createKeyboard, t2);
        createKeyboard = courseRepository.connectToProgram(createKeyboard, educationProgram);

        Course databaseCreating = courseRepository.save(new Course(36, 3, AttestationForm.EXAM, "Database creating"));
        Course turboLearning = courseRepository.save(new Course(36, 3, AttestationForm.EXAM, "Turbo learning system"));
        databaseCreating = courseRepository.connectToTemplate(databaseCreating, t3);
        databaseCreating = courseRepository.connectToProgram(databaseCreating, educationProgram);
        turboLearning = courseRepository.connectToTemplate(turboLearning, t3);
        turboLearning = courseRepository.connectToProgram(turboLearning, educationProgram);

        // Things, what courses develops
        // Programming
        skillsRepository.connectToCourse(S1, programming);
        skillsRepository.connectToCourse(S2, programming);
        knowledgeRepository.connectToCourse(KC1, programming);

        // Physics
        knowledgeRepository.connectToCourse(KM1, physics);
        knowledgeRepository.connectToCourse(KM2, physics);

        // Create server
        SM2 = skillsRepository.connectToCourse(SM2, createServer);
        SW1 = skillsRepository.connectToCourse(SW1, createServer);
        SC1 = skillsRepository.connectToCourse(SC1, createServer);
        KW1 = knowledgeRepository.connectToCourse(KW1, createServer);
        KW3 = knowledgeRepository.connectToCourse(KW3, createServer);

        // Write web application
        SW1 = skillsRepository.connectToCourse(SW1, writeWebApplication);
        SW2 = skillsRepository.connectToCourse(SW2, writeWebApplication);
        knowledgeRepository.connectToCourse(KW1, writeWebApplication);
        KW2 = knowledgeRepository.connectToCourse(KW2, writeWebApplication);
        knowledgeRepository.connectToCourse(KW3, writeWebApplication);

        // Create keyboard
        skillsRepository.connectToCourse(SM2, createKeyboard);
        skillsRepository.connectToCourse(SC1, createKeyboard);

        // Database
        skillsRepository.connectToCourse(SW1, databaseCreating);

        // Turbo learning
        skillsRepository.connectToCourse(SM1, turboLearning);
        skillsRepository.connectToCourse(SW2, turboLearning);
        knowledgeRepository.connectToCourse(KW2, turboLearning);
    }
}