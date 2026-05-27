package com.example.educacion;

import com.example.educacion.entity.Rol;
import com.example.educacion.entity.Usuario;
import com.example.educacion.repository.RolRepository;
import com.example.educacion.repository.UsuarioRepository;
import com.example.educacion.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EducacionApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	void contextLoads() {
	}

	@Test
	@WithMockUser(roles = "ESTUDIANTE")
	void estudianteCanGetCursos() throws Exception {
		mockMvc.perform(get("/api/cursos"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ESTUDIANTE")
	void estudianteCanGetNotasPorEstudiante() throws Exception {
		mockMvc.perform(get("/api/notas/estudiante/1"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ESTUDIANTE")
	void estudianteCannotCreateCurso() throws Exception {
		mockMvc.perform(post("/api/cursos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\":\"Matematicas\",\"descripcion\":\"Curso de Matematicas\",\"creditos\":4}"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ESTUDIANTE")
	void estudianteCannotGetGeneralNotas() throws Exception {
		mockMvc.perform(get("/api/notas"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "USER")
	void userCanGetGeneralNotas() throws Exception {
		mockMvc.perform(get("/api/notas"))
				.andExpect(status().isOk());
	}

	@Test
	void testTokenLogoutAndRevocation() throws Exception {
		// 1. Asegurar que exista un usuario de prueba en la base de datos
		String email = "test-logout-user@test.com";
		if (usuarioRepository.findByEmail(email).isEmpty()) {
			Usuario usuario = new Usuario();
			usuario.setNombres("Test");
			usuario.setApellidos("Logout");
			usuario.setEmail(email);
			usuario.setPassword("password");

			Rol rol = rolRepository.findByNombre("USER")
					.orElseGet(() -> rolRepository.save(new Rol(null, "USER")));
			Set<Rol> roles = new HashSet<>();
			roles.add(rol);
			usuario.setRoles(roles);

			usuarioRepository.save(usuario);
		}

		// 2. Generar un token real para este usuario
		String token = jwtUtil.generarToken(email);
		String authHeader = "Bearer " + token;

		// 3. Verificar que el token funcione correctamente para acceder a cursos (debe
		// dar 200 OK)
		mockMvc.perform(get("/api/cursos")
				.header("Authorization", authHeader))
				.andExpect(status().isOk());

		// 4. Llamar al endpoint de logout para revocar este token (debe dar 200 OK)
		mockMvc.perform(post("/api/auth/logout")
				.header("Authorization", authHeader))
				.andExpect(status().isOk())
				.andExpect(content().string("Sesión cerrada exitosamente"));

		// 5. Intentar usar el token revocado nuevamente. Debe dar 403 Forbidden o ser
		// bloqueado por la seguridad.
		mockMvc.perform(get("/api/cursos")
				.header("Authorization", authHeader))
				.andExpect(status().isForbidden());
	}
}