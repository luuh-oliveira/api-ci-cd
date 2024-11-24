package school.sptech.avaliacaocontinuada1;

import java.time.LocalDate;
import java.lang.reflect.Field;

public class UsuarioMock {

    public static Object getInstanceForQuery(
            int id,
            String nome,
            String email,
            String senha,
            String dataNascimento) {
        try {
            
            Class<?> clazz = Class.forName("school.sptech.avaliacaocontinuada1.Usuario");
            Object usuario = clazz.getDeclaredConstructor().newInstance();

            setField(usuario, "id", id);
            setField(usuario, "nome", nome);
            setField(usuario, "email", email);
            setField(usuario, "senha", senha);
            setField(usuario, "dataNascimento", LocalDate.parse(dataNascimento));

            return usuario;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void setField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
