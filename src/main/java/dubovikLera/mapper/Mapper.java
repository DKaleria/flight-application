package dubovikLera.mapper;

public interface Mapper <T, F>{
    T mapFrom(F f);
}
