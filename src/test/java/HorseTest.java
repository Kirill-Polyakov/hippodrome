import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HorseTest {
    private Horse horse;

    @BeforeEach
    void beforeEach() {
        horse = new Horse("Name", 2.5, 1);
    }

    @Test
    @DisplayName("""
             Проверка выбрасываемого исключения IllegalArgumentException, при передаче в конструктор:
             первым параметром null,
             вторым параметром отрицательного значения,
             третьим параметром отрицательного значения.
            """)
    void checkConstructorArguments() {
        //then
        assertAll(
                "Grouped Assertions of Horse Constructor",
                () -> assertThrows(IllegalArgumentException.class, () -> new Horse(null, horse.getSpeed(), horse.getDistance())),
                () -> assertThrows(IllegalArgumentException.class, () -> new Horse(horse.getName(), -1, horse.getDistance())),
                () -> assertThrows(IllegalArgumentException.class, () -> new Horse(horse.getName(), horse.getSpeed(), -1))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "    "
    })
    @DisplayName("""
            1) Проверка выбрасываемого исключения IllegalArgumentException, при передаче в конструктор первым параметром: пустой строки, пробела, табуляции.
            2) Проверка СООБЩЕНИЯ, при выбрасывании IllegalArgumentException, после выполнения первого пункта.
            """)
    void checkConstructorExceptionWithFirstParamIsBlank(String name) {
        //given
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new Horse(name, 2.5, 1));
        //then
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Horse(name, 2.5, 1)),
                () -> assertEquals("Name cannot be blank.", illegalArgumentException.getMessage())
        );
    }

    @Test
    @DisplayName("""
             Проверка сообщений, при выбрасывании исключения IllegalArgumentException, при передаче в конструктор:
             первым параметром null,
             вторым параметром отрицательного значения,
             третьим параметром отрицательного значения.
            """)
    void heckMessagesFromIllegalArgumentException() {
        //given
        IllegalArgumentException nameEqualsNull = assertThrows(IllegalArgumentException.class, () -> new Horse(null, horse.getSpeed(), horse.getDistance()));
        IllegalArgumentException speedEqualsNull = assertThrows(IllegalArgumentException.class, () -> new Horse(horse.getName(), -1, horse.getDistance()));
        IllegalArgumentException distanceEqualsNull = assertThrows(IllegalArgumentException.class, () -> new Horse(horse.getName(), horse.getSpeed(), -1));
        //then
        assertAll(
                "Grouped Message from IllegalArgumentException",
                () -> assertEquals("Name cannot be null.", nameEqualsNull.getMessage()),
                () -> assertEquals("Speed cannot be negative.", speedEqualsNull.getMessage()),
                () -> assertEquals("Distance cannot be negative.", distanceEqualsNull.getMessage())
        );
    }

    @Test
    @DisplayName("Проверка возвращаемого значения, методом getName")
    void getName() {
        //then
        assertEquals("Name", horse.getName());
    }

    @Test
    @DisplayName("Проверка возвращаемого значения, методом getSpeed")
    void getSpeed() {
        //then
        assertEquals(2.5, horse.getSpeed());
    }

    @Test
    @DisplayName("""
            1) Проверка возвращаемого значения, методом getDistance.
            2) Проверка возвращаемого значения, методом getDistance, при создании объекта с помощью конструктора с 2 параметрами
            """)
    void getDistance() {
        //given
        Horse horseWith2Param = new Horse("Name", 2.5);
        //then
        assertAll(
                () -> assertEquals(1, horse.getDistance()),
                () -> assertEquals(0, horseWith2Param.getDistance())
        );
    }

    @Test
    @DisplayName("Проверка, что метод вызывает внутри метод getRandomDouble с параметрами 0.2 и 0.9")
    void moveCallsGetRandomDoubleWithCorrectParameters() {
        //then
        try (MockedStatic<Horse> horseGetRandomMock = Mockito.mockStatic(Horse.class)) {
            horse.move();
            horseGetRandomMock.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.2, 0.5, 0.7})
    @DisplayName("Проверка, что метод присваивает дистанции значение высчитанное по формуле: distance + speed * getRandomDouble(0.2, 0.9)")
    void checkReturnedDistance(double randomValue) {
        try (MockedStatic<Horse> horseMockedStatic = Mockito.mockStatic(Horse.class)) {
            horseMockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(randomValue);
            double expectedDistance = horse.getDistance() + horse.getSpeed() * Horse.getRandomDouble(0.2, 0.9);
            horse.move();
            assertEquals(expectedDistance, horse.getDistance());
        }
    }
}