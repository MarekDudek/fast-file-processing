package com.marekdudek.fastfileprocessing;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.out;

public class ReadingFileTest {

    private static final String PATH = "/home/marek/Devel/git-repos/test-files/google.txt";

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

        out.printf("took %s to process %d lines (%d characters)", timer.stop(), lines, characters);
    }

    @Test
    public void with_file_input_stream() throws FileNotFoundException {

        final FileInputStream stream = new FileInputStream(PATH);
    }
}
