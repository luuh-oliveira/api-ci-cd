package school.sptech.avaliacaocontinuada1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final List<Usuario> usuarios = new ArrayList<>();
    private int idUsuarioAtual = 0;

    @PostMapping
    public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario usuario) {

        if (emailValido(usuario.getEmail())) {
            if (!emailExistente(usuario.getEmail())) {
                usuario.setId(idUsuarioAtual += 1);
                usuarios.add(usuario);
                return ResponseEntity.status(201).body(usuario);
            }
            return ResponseEntity.status(409).build();
        } else {
            return ResponseEntity.status(400).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {

        if (usuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(usuarios);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioPorId(@PathVariable int id) {

        Usuario usuarioProcurado = findUsuario(id);

        if (usuarioProcurado != null) {
            return ResponseEntity.status(200).body(usuarioProcurado);
        }
        return ResponseEntity.status(404).build();

    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id, @RequestBody Usuario usuario) {

        if (emailValido(usuario.getEmail())) {

            for (int i = 0; i < usuarios.size(); i++) {
                Usuario usuarioAtual = usuarios.get(i);
                if (usuarioAtual.getId() == id) {
                    if (!emailExistente(usuario.getEmail())
                            || usuarioAtual.getEmail().equals(usuario.getEmail())) {
                        usuario.setId(usuarioAtual.getId());
                        usuarios.set(i, usuario);
                        return ResponseEntity.status(200).body(usuarios.get(i));
                    } else {
                        return ResponseEntity.status(409).build();
                    }
                }
            }
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(400).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deleteUsuario(@PathVariable int id) {
        Usuario usuarioProcurado = findUsuario(id);

        if (usuarioProcurado != null) {

            usuarios.remove(usuarioProcurado);
            return ResponseEntity.status(200).body(usuarioProcurado);

        }

        return ResponseEntity.status(404).build();

    }

    private Usuario findUsuario(int id) {
        Usuario usuarioProcurado = null;
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                usuarioProcurado = usuario;
            }
        }
        return usuarioProcurado;
    }

    public boolean emailValido(String email) {
        return !email.isBlank() && email.length() >= 10 && email.contains("@");
    }

    public boolean emailExistente(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

}
