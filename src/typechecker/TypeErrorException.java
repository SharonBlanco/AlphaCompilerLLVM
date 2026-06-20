package typechecker;

/**
 * Excepción personalizada que representa un error de tipos detectado
 * durante el análisis semántico del compilador AlphaCompiler.
 *
 * <p>Se usa dentro del TypeChecker para señalar situaciones donde los tipos
 * son incompatibles o inválidos. Se captura localmente con try-catch para
 * que el análisis pueda continuar y acumular todos los errores en lugar de
 * detenerse al primer fallo.</p>
 *
 * <p>TODO: Agregar un constructor con mensaje descriptivo para facilitar
 * el diagnóstico. Usar try-catch específico para esta excepción
 * (no para RuntimeException en general).</p>
 */
public class TypeErrorException extends RuntimeException {
    // Todo, message constructor, pasar mensaje, mandar texto
    // try catch para esta excepcion no para runtime
}