package com.bitchat.android.ai

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DisasterDetectorTest {

    private val detector = DisasterDetector()

    @Test
    fun detect_returnsTrue_whenTextContainsDisasterKeywords() {
        assertTrue(detector.detect("Help me, there is a fire!", null))
        assertTrue(detector.detect("Flood in the area", null))
        assertTrue(detector.detect("Emergency reported", null))
        assertTrue(detector.detect("Many casualties", null))
        assertTrue(detector.detect("We are trapped", null))
    }

    @Test
    fun detect_returnsFalse_whenTextIsSafe() {
        assertFalse(detector.detect("Hello world", null))
        assertFalse(detector.detect("Nice weather today", null))
        assertFalse(detector.detect("Just checking in", null))
    }

    @Test
    fun detect_isCaseInsensitive() {
        assertTrue(detector.detect("HELP ME", null))
        assertTrue(detector.detect("fIrE", null))
    }

    @Test
    fun detect_returnsTrue_whenImageIsProvided() {
        // We simulate a "test_disaster" path which triggers the detection in our mock logic
        assertTrue(detector.detect(null, "/tmp/test_disaster.jpg"))
    }

    @Test
    fun detect_returnsFalse_whenImageDoesnotExist() {
         assertFalse(detector.detect(null, "/tmp/non_existent_safe_image.jpg"))
    }
}
