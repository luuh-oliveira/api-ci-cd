package school.sptech.avaliacaocontinuada1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Field;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioController usuarioController;

    @Nested
    @DisplayName("GET")
    class Get {

        @Nested
        @DisplayName("Listagem")
        class Listagem {

            @Test
            @DisplayName("Quando não há usuários, então retorna status 204")
            void quandoNaoHaUsuariosEntaoRetornaStatus204() throws Exception {

                configureListEmpty();

                mockMvc.perform(get("/usuarios"))
                        .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("Quando há usuários, então retorna status 200")
            void quandoHaUsuariosEntaoRetornaStatus200() throws Exception {

                configureListOfMocks();

                mockMvc.perform(get("/usuarios"))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("Busca por ID")
        class BuscaPorId {

            @Test
            @DisplayName("Quando o ID não existe, então retorna status 404")
            void quandoOIdNaoExisteEntaoRetornaStatus404() throws Exception {

                mockMvc.perform(get("/usuarios/1"))
                        .andExpect(status().isNotFound());
            }

            @Test
            @DisplayName("Quando o ID existe, então retorna status 200")
            void quandoOIdExisteEntaoRetornaStatus200() throws Exception {

                configureListOfMocks();

                mockMvc.perform(get("/usuarios/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.nome").value("Fulano"));
            }
        }
    }

    @Nested
    @DisplayName("POST")
    class Post {

        @Test
        @DisplayName("Quando o e-mail não contém @, então retorna status 400")
        void quandoOEmailNaoContemArrobaEntaoRetornaStatus400() throws Exception {

            String body = """
                        {
                            "nome": "Fulano",
                            "email": "fulano",
                            "senha": "123456",
                            "dataNascimento": "1990-01-01"
                        }
                    """;

            mockMvc.perform(post("/usuarios")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Quando o e-mail contém menos que 8 caracteres, então retorna status 400")
        void quandoOEmailContemMenosQue8CaracteresEntaoRetornaStatus400() throws Exception {

            String body = """
                        {
                            "nome": "Fulano",
                            "email": "a@b.c",
                            "senha": "123456",
                            "dataNascimento": "1990-01-01"
                        }
                    """;

            mockMvc.perform(post("/usuarios")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Quando o e-mail já existe, então retorna status 409")
        void quandoOEmailJaExisteEntaoRetornaStatus409() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Beltrano",
                            "email": "fulano@gmail.com",
                            "senha": "123456",
                            "dataNascimento": "2003-01-01"
                        }
                    """;

            mockMvc.perform(post("/usuarios")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Quando o e-mail é único, então retorna status 201")
        void quandoOEmailEUnicoEntaoRetornaStatus201() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Beltrano",
                            "email": "beltrano@beltrano.com",
                            "senha": "123456",
                            "dataNascimento": "2003-01-01"
                        }
                    """;

            mockMvc.perform(post("/usuarios")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nome").value("Beltrano"));
        }
    }

    @Nested
    @DisplayName("PUT")
    class Put {

        @Test
        @DisplayName("Quando o ID não existe, e lista está vazia, então retorna status 404")
        void quandoOIdNaoExisteEListaEstaVaziaEntaoRetornaStatus404() throws Exception {

            configureListEmpty();

            String body = """
                        {
                            "nome": "Alteracao",
                            "email": "alteracao@gmail",
                            "senha": "123456",
                            "dataNascimento": "1990-01-01"
                        }
                    """;

            mockMvc.perform(put("/usuarios/1")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isNotFound());
        }


        @Test
        @DisplayName("Quando o ID não existe, e lista nao está vazia, então retorna status 404")
        void quandoOIdNaoExisteEntaoRetornaStatus404() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Alteracao",
                            "email": "alteracao@gmail",
                            "senha": "123456",
                            "dataNascimento": "1990-01-01"
                        }
                    """;

            mockMvc.perform(put("/usuarios/100")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Quando o ID existe, e nao possui @, então retorna status 400")
        void quandoOIdExisteMasEmailNaoPossuiArrobaEntaoRetorna400() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Alteracao",
                            "email": "alteracao",
                            "senha": "123456",
                            "dataNascimento": "1990-01-01"
                        }
                    """;

            mockMvc.perform(put("/usuarios/1")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Quando o ID existe, e email possui menos de 8 caracteres, então retorna status 400")
        void quandoOIdExisteMasEmailPossuiMenosDe8CaracteresEntaoRetorna400() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Alteracao",
                            "email": "a@b.c",
                            "senha": "123456",
                            "dataNascimento": "1990-01-01"
                        }
                    """;

            mockMvc.perform(put("/usuarios/1")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Quando o ID existe, e email já existe, então retorna status 409")
        void quandoOIdExisteMasEmailJaExisteEntaoRetorna409() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Alteracao",
                            "email": "ciclano@yahoo",
                            "senha": "123456",
                            "dataNascimento": "1990-01-01"
                        }
                    """;

            mockMvc.perform(put("/usuarios/1")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Quando id existe, e o email permanece o mesmo, então retorna status 200")
        void quandoIdExisteEEmailPermaneceOMesmoEntaoRetorna200() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Alteracao",
                            "email": "fulano@gmail.com",
                            "senha": "424242",
                            "dataNascimento": "2011-01-01"
                        }
                    """;

            mockMvc.perform(put("/usuarios/1")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Alteracao"));
        }

        @Test
        @DisplayName("Quando id existe, e o email é único, então retorna status 200")
        void quandoIdExisteEEmailEUnicoEntaoRetorna200() throws Exception {

            configureListOfMocks();

            String body = """
                        {
                            "nome": "Alteracao",
                            "email": "alteracao@alteracao.com",
                            "senha": "424242",
                            "dataNascimento": "2011-01-01"
                        }
                    """;

            mockMvc.perform(put("/usuarios/1")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Alteracao"));
        }
    }

    @Nested
    @DisplayName("DELETE")
    class Delete {

        @Test
        @DisplayName("Quando o ID não existe, e lista está vazia, então retorna status 404")
        void quandoOIdNaoExisteEntaoRetornaStatus404() throws Exception {

            configureListEmpty();

            mockMvc.perform(delete("/usuarios/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Quando o ID não existe, e lista não está vazia, então retorna status 404")
        void quandoOIdNaoExisteEListaNaoEstaVaziaEntaoRetornaStatus404() throws Exception {

            configureListOfMocks();

            mockMvc.perform(delete("/usuarios/100"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Quando o ID existe, então retorna status 200")
        void quandoOIdExisteEntaoRetornaStatus200() throws Exception {

            configureListOfMocks();

            mockMvc.perform(delete("/usuarios/1"))
                    .andExpect(status().isOk());
        }
    }


    private void configureListOfMocks() {

        try {

            Field usuariosField = usuarioController.getClass().getDeclaredField("usuarios");
            usuariosField.setAccessible(true);

            List<Usuario> usuarios = (List<Usuario>) usuariosField.get(usuarioController);

            usuarios.clear();

            Usuario usuario1 = (Usuario) UsuarioMock.getInstanceForQuery(
                    1,
                    "Fulano",
                    "fulano@gmail.com",
                    "123456",
                    "1990-01-01");

            Usuario usuario2 = (Usuario) UsuarioMock.getInstanceForQuery(
                    2,
                    "Ciclano",
                    "ciclano@yahoo",
                    "654321",
                    "2000-01-01");

            usuarios.add(usuario1);
            usuarios.add(usuario2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configureListEmpty() {
        try {
            Field usuariosField = usuarioController.getClass().getDeclaredField("usuarios");
            usuariosField.setAccessible(true);

            List<Usuario> usuarios = (List<Usuario>) usuariosField.get(usuarioController);

            usuarios.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
