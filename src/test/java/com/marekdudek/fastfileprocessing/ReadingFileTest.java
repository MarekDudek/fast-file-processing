package com.marekdudek.fastfileprocessing;

import com.google.common.base.Stopwatch;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReadingFileTest {

    private static final String PATH = "/home/marek/Devel/git-repos/test-files/google.txt";

    private static final long LINES = 36_268_229;
    private static final long CHARACTERS = 1_073_479_832;

    @Test
    public void with_scanner() throws FileNotFoundException {

        final Stopwatch timer = Stopwatch.createStarted();

        final File file = new File(PATH);
        final Scanner scanner = new Scanner(file).useDelimiter("\n");

        long lines = 0;
        long characters = 0;
        while (scanner.hasNext()) {
            final String line = scanner.next();
            characters += line.length();
            lines++;
        }
        scanner.close();

        assertThat(lines, is(equalTo(LINES)));
        assertThat(characters, is(equalTo(CHARACTERS)));
        out.printf("took %s to process %d lines (%d characters)%n", timer.stop(), lines, characters);
    }

    @Test
    @Ignore
    public void with_byte_input_stream() throws IOException {

        final Stopwatch timer = Stopwatch.createStarted();

        final FileInputStream stream = new FileInputStream(PATH);

        long lines = 0;
        long characters = 0;
        int character;
        while ((character = stream.read()) != -1) {
            characters++;
            if (character == (int) '\n')
                lines++;
        }
        stream.close();

        out.printf("took %s to process %d lines (%d characters)%n", timer.stop(), lines, characters);
    }

    @Test
    @Ignore
    public void with_char_input_stream() throws IOException {

        final Stopwatch timer = Stopwatch.createStarted();
        long characters = 0;
        long lines = 0;

        int character;
        final FileReader reader = new FileReader(PATH);

        while ((character = reader.read()) != -1) {
            characters++;
            if (character == (int) ('\n'))
                lines++;
        }
        reader.close();

        out.printf("took %s to process %d lines (%d characters)%n", timer.stop(), lines, characters);
    }

    @Test
    public void with_buffered_reader() throws IOException {

        final Stopwatch timer = Stopwatch.createStarted();
        long characters = 0;
        long lines = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                characters += line.length();
                lines++;
            }
        }

        out.printf("took %s to process %d lines (%d characters)%n", timer.stop(), lines, characters);
    }

    @Test
    public void with_memory_mapped_file() throws IOException {

        final Stopwatch timer = Stopwatch.createStarted();
        long characters = 0;
        long lines = 1;

        final FileChannel channel = new RandomAccessFile(new File(PATH), "r").getChannel();
        final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size() -1);

        while (buffer.position() < buffer.limit()) {
            final byte c = buffer.get();
            characters++;
            if (c == '\n') lines++;
        }

       /* assertThat(lines, is(equalTo(LINES)));
        assertThat(characters, is(equalTo(CHARACTERS)));*/

        out.printf("took %s to process %d lines (%d characters)%n", timer.stop(), lines, characters);
    }
}
