package thiagosbarros.com.servicehub;

public class UsuarioNaoEncontradoException extends RuntimeException {
  public UsuarioNaoEncontradoException(String message) {
    super(message);
  }
}
