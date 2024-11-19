package apap.tutorial.manpromanpro;

import apap.tutorial.manpromanpro.model.Developer;
import apap.tutorial.manpromanpro.model.Pekerja;
import apap.tutorial.manpromanpro.model.Proyek;
import apap.tutorial.manpromanpro.model.Role;
import apap.tutorial.manpromanpro.model.UserModel;
import apap.tutorial.manpromanpro.repository.RoleDb;
import apap.tutorial.manpromanpro.repository.UserDb;
import apap.tutorial.manpromanpro.restservice.UserRestService;
import apap.tutorial.manpromanpro.service.DeveloperService;
import apap.tutorial.manpromanpro.service.PekerjaService;
import apap.tutorial.manpromanpro.service.ProyekService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ManpromanproApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManpromanproApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(RoleDb roleDb, UserDb userDb, UserRestService userService, ProyekService proyekService, DeveloperService developerService, PekerjaService pekerjaService) {
		return args -> {
			if (roleDb.findByRole("Admin").orElse(null) == null) {
				Role role = new Role();
				role.setRole("Admin");
				roleDb.save(role);
			}

			if (roleDb.findByRole("PM").orElse(null) == null) {
				Role role = new Role();
				role.setRole("PM");
				roleDb.save(role);
			}

			if (roleDb.findByRole("HR").orElse(null) == null) {
				Role role = new Role();
				role.setRole("HR");
				roleDb.save(role);
			}

			UserModel user;

			if (userDb.findByUsername("admin") == null) {
				user = new UserModel();
				user.setName("Admin");
				user.setUsername("admin");
				user.setPassword(userService.hashPassword("admin"));
				user.setRole(roleDb.findByRole("Admin").orElse(null));
				userDb.save(user);
			}

			if (userDb.findByUsername("pm") == null) {
				user = new UserModel();
				user.setName("PM");
				user.setUsername("pm");
				user.setPassword(userService.hashPassword("pm"));
				user.setRole(roleDb.findByRole("PM").orElse(null));
				userDb.save(user);
			}

			if (userDb.findByUsername("hr") == null) {
				user = new UserModel();
				user.setName("HR");
				user.setUsername("hr");
				user.setPassword(userService.hashPassword("hr"));
				user.setRole(roleDb.findByRole("HR").orElse(null));
				userDb.save(user);
			}

			var faker = new Faker(new Locale("in-ID"));

			var proyek = new Proyek();
			var fakeProyek = faker.leagueOfLegends();
			var fakeDate = faker.date();

			proyek.setNama(fakeProyek.champion());
			proyek.setDeskripsi(fakeProyek.quote());
			proyek.setTanggalMulai(fakeDate.past(2, TimeUnit.DAYS));
			proyek.setTanggalSelesai(fakeDate.future(2, TimeUnit.DAYS));
			proyek.setCreatedAt(new Date());
			proyek.setUpdatedAt(new Date());
			proyek.setStatus("STARTED");
			proyek.setDeletedAt(null);

			var developer = new Developer();
			var fakeDeveloper = faker.name();
			var fakeAddress = faker.address();

			developer.setNama(fakeDeveloper.fullName());
			developer.setAlamat(fakeAddress.fullAddress());
			developer.setTanggalBerdiri(fakeDate.birthday());
			developer.setEmail("fakedeveloper@test.com");
			developer.setCreatedAt(new Date());
			developer.setUpdatedAt(new Date());

			var newDeveloper = developerService.addDeveloper(developer);
			proyek.setDeveloper(newDeveloper);
	
			var pekerja = new Pekerja();
			pekerja.setNama(faker.name().fullName());
			String pekerjaan;
			do {
				pekerjaan = faker.job().title(); // Generate a random job title
			} while (pekerjaan.length() > 30); // Repeat until it fits the size constraint

			pekerja.setPekerjaan(pekerjaan);
			pekerja.setUsia(faker.number().numberBetween(18, 65));
			pekerja.setBiografi(faker.lorem().paragraph());
			pekerja.setCreatedAt(new Date());
			pekerja.setUpdatedAt(new Date());

			var newPekerja = pekerjaService.addPekerja(pekerja);

			List<Pekerja> listPekerja = new ArrayList<>();
			listPekerja.add(newPekerja);
			proyek.setListPekerja(listPekerja);

			proyekService.addProyek(proyek);
		};
	}
}
