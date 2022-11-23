import org.example.Files.Settings;
import org.example.Logger.Logger;
import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.example.Main.logger;

public class SettingsTest {

    @ParameterizedTest
    @MethodSource("source")
    public void localeTest(String fileName, Settings expected){
        logger = Logger.getInstance();
        Settings settings=new Settings();
        Settings result = settings.getSettings(fileName);

        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("Settings.json",       new Settings(8888, "127.0.0.1", "Client"))
        );
    }




}
