package game.model.entities;

/**
 * An EntityVisitor is a class that gives several options for Entities to call themselves in order
 * to access different functionality.
 * @param <T> The return type for function performed on the Entity.
 */
public interface EntityVisitor<T> {
  /**
   * The function for an entity to call if it is a Duck.
   * @param d The duck entity handing itself back to the code with a specific type this time.
   * @return The function returns whatever you want for a duck, of type T.
   */
  T visitDuck(Duck d);
  T visitKoala(Koala k);
}
