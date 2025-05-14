import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HippodromeTest {

    Hippodrome hippodrome;

    @Test
    @DisplayName("""
            1) Проверка, что при передаче в конструктор null, будет выброшено IllegalArgumentException.
            2) Проверка, что при передаче в конструктор пустого списка, будет выброшено IllegalArgumentException.
            """)
    void callConstructorWithNull() {
        assertAll(
                "проверка выбрасываемых исключений при создании объекта с парамтром null и пустым списком",
                () -> assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<>()))
        );
    }

    @Test
    @DisplayName("""
            1) Проверка, что при передаче в конструктор null, выброшенное исключение будет содержать сообщение "Horses cannot be null.".
            2) Проверка, что при передаче в конструктор пустого списка, выброшенное исключение будет содержать сообщение "Horses cannot be empty.".
            """)
    void checkMessageIfConstructorCastException() {
        //given
        IllegalArgumentException exceptionWithNullInParam = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
        IllegalArgumentException emptyList = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<>()));
        //then
        assertAll(
                "",
                () -> assertEquals("Horses cannot be null.", exceptionWithNullInParam.getMessage()),
                () -> assertEquals("Horses cannot be empty.", emptyList.getMessage())
        );
    }

    @Test
    @DisplayName("Проверка, что метод возвращает список, который содержит те же объекты и в той же последовательности, что и список который был передан в конструктор.")
    void getHorses() {
        //given
        List<Horse> testList = new ArrayList<>() {
            {
                for (int i = 0; i < 30; i++) {
                    add(new Horse("" + i, Horse.getRandomDouble(.2, .9)));
                }
            }
        };
        hippodrome = new Hippodrome(testList);
        //then
        assertEquals(testList, hippodrome.getHorses());
    }

    @Test
    @DisplayName("Проверка, что метод вызывает метод move у всех лошадей.")
    void move() {
        //given
        List<Horse> horses = new ArrayList<>(){
            {
                for (int i = 0; i < 50; i++) {
                    add(Mockito.mock(Horse.class));
                }
            }
        };
        hippodrome = new Hippodrome(horses);
        //then
        hippodrome.move();
        for (Horse hors : horses) {
            Mockito.verify(hors).move();
        }
    }

    @Test
    @DisplayName("Проверка, что метод возвращает лошадь с самым большим значением distance.")
    void getWinner() {
        //given
        Horse horse1 = new Horse("Name1", 1, 1);
        Horse horse2 = new Horse("Name2", 2, 2);
        Horse horse3 = new Horse("Name3", 3, 3);
        List<Horse> testList = List.of(horse1, horse2, horse3);
        hippodrome = new Hippodrome(testList);
        //then
        assertEquals(horse3, hippodrome.getWinner());
    }
}