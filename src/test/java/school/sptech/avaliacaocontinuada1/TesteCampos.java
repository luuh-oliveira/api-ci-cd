package school.sptech.avaliacaocontinuada1;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Teste de atributos e encapsulamento")
public class TesteCampos {

    @Nested
    @DisplayName("Teste atributos Usuario")
    class TesteCamposUsuario {

        @Test
        @DisplayName("Atributos do usuario devem estar conforme enunciado")
        void classeUsuarioDevemTerCamposConformeEnunciado() {

            Class<Usuario> clazz = Usuario.class;

            List<String> campos = List.of("id", "nome", "email", "senha", "dataNascimento");

            for (String atributo : campos) {

                Assertions.assertDoesNotThrow(() -> {
                    clazz.getDeclaredField(atributo);
                });
            }
        }

        @Test
        @DisplayName("Usuario deve possuir getters e setters para todos os atributos")
        void usuarioDevePossuirGettersESetters() {

            Class<Usuario> clazz = Usuario.class;

            List<String> campos = List.of("id", "nome", "email", "senha", "dataNascimento");

            for (String atributo : campos) {

                String nomeMetodo = atributo.substring(0, 1).toUpperCase() + atributo.substring(1);

                Assertions.assertDoesNotThrow(() -> {
                    clazz.getDeclaredMethod("get" + nomeMetodo);
                    clazz.getDeclaredMethod("set" + nomeMetodo, clazz.getDeclaredField(atributo).getType());
                });
            }
        }
    }

    @Nested
    @DisplayName("Teste Atributos UsuarioController")
    class TesteCamposUsuarioController {

        @Test
        @DisplayName("UsuarioController deve ter atributo conforme enunciado (usuarios)")
        void classeUsuarioControllerDevemTerCamposConformeEnunciado() {

            Class<UsuarioController> clazz = UsuarioController.class;

            List<String> campos = List.of("usuarios");

            for (String atributo : campos) {
                Assertions.assertDoesNotThrow(() -> {
                    clazz.getDeclaredField(atributo);
                });
            }
        }
    }
}
