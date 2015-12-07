package org.forgerock.cuppa;

import static org.forgerock.cuppa.Assertions.assertTestResources;
import static org.forgerock.cuppa.Cuppa.*;
import static org.mockito.Mockito.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HookExceptionTests {

    @BeforeMethod
    public void setup() {
        Cuppa.reset();
    }

    @Test
    public void shouldReturnSingleErrorResultIfBeforeHookThrowsException() {

        //Given
        Function beforeFunction = mock(Function.class, "beforeFunction");

        doThrow(new RuntimeException("Before failed")).when(beforeFunction).apply();

        {
            describe("before blocks", () -> {
                before(beforeFunction);
                it("a test", () -> {
                });
                it("a second test", () -> {
                });
            });
        }

        //When
        TestResults results = Cuppa.runTests();

        //Then
        assertTestResources(results, 0, 0, 1);
    }

    @Test
    public void shouldRunAfterHookIfBeforeHookThrowsException() {

        //Given
        Function beforeFunction = mock(Function.class, "beforeFunction");
        Function afterFunction = mock(Function.class, "afterFunction");

        doThrow(new RuntimeException("Before failed")).when(beforeFunction).apply();

        {
            describe("before blocks", () -> {
                before(beforeFunction);
                after(afterFunction);
                it("a test", () -> {
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(afterFunction).apply();
    }

    @Test
    public void shouldSkipBeforeEachHookIfBeforeHookThrowsException() {

        //Given
        Function beforeFunction = mock(Function.class, "beforeFunction");
        Function beforeEachFunction = mock(Function.class, "beforeEachFunction");

        doThrow(new RuntimeException("Before failed")).when(beforeFunction).apply();

        {
            describe("before blocks", () -> {
                before(beforeFunction);
                beforeEach(beforeEachFunction);
                it("a test", () -> {
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(beforeEachFunction, never()).apply();
    }

    @Test
    public void shouldSkipAfterEachHookIfBeforeHookThrowsException() {

        //Given
        Function beforeFunction = mock(Function.class, "beforeFunction");
        Function afterEachFunction = mock(Function.class, "afterEachFunction");

        doThrow(new RuntimeException("Before failed")).when(beforeFunction).apply();

        {
            describe("before blocks", () -> {
                before(beforeFunction);
                afterEach(afterEachFunction);
                it("a test", () -> {
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(afterEachFunction, never()).apply();
    }

    @Test
    public void shouldSkipTestsIfBeforeHookThrowsException() {

        //Given
        Function beforeFunction = mock(Function.class, "beforeFunction");
        Function testFunction = mock(Function.class, "testFunction");

        doThrow(new RuntimeException("Before failed")).when(beforeFunction).apply();

        {
            describe("before blocks", () -> {
                before(beforeFunction);
                it("a test", testFunction);
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(testFunction, never()).apply();
    }

    @Test
    public void shouldSkipNestedBlocksIfBeforeThrowsException() {

        //Given
        Function topLevelBeforeFunction = mock(Function.class, "topLevelBeforeFunction");
        Function nestedBeforeFunction = mock(Function.class, "nestedBeforeFunction");
        Function nestedBeforeEachFunction = mock(Function.class, "nestedBeforeEachFunction");
        Function nestedAfterEachFunction = mock(Function.class, "nestedAfterEachFunction");
        Function nestedAfterFunction = mock(Function.class, "nestedAfterFunction");
        Function nestedTestFunction = mock(Function.class, "nestedTestFunction");

        doThrow(new RuntimeException("Before failed")).when(topLevelBeforeFunction).apply();

        {
            describe("before blocks", () -> {
                before(topLevelBeforeFunction);
                when("the first 'before' block throws an exception", () -> {
                    before(nestedBeforeFunction);
                    beforeEach(nestedBeforeEachFunction);
                    afterEach(nestedAfterEachFunction);
                    after(nestedAfterFunction);
                    it("doesn't run the test nested", nestedTestFunction);
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(nestedBeforeFunction, never()).apply();
        verify(nestedBeforeEachFunction, never()).apply();
        verify(nestedAfterEachFunction, never()).apply();
        verify(nestedAfterFunction, never()).apply();
        verify(nestedTestFunction, never()).apply();
    }

    @Test
    public void shouldReturnSingleErrorResultIfBeforeEachHookThrowsException() {

        //Given
        Function beforeEachFunction = mock(Function.class, "beforeEachFunction");

        doThrow(new RuntimeException("Before each failed")).when(beforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                beforeEach(beforeEachFunction);
                it("a test", () -> {
                });
                it("a second test", () -> {
                });
            });
        }

        //When
        TestResults results = Cuppa.runTests();

        //Then
        assertTestResources(results, 0, 0, 1);
    }

    @Test
    public void shouldRunAfterHookIfBeforeEachHookThrowsException() {

        //Given
        Function beforeEachFunction = mock(Function.class, "beforeEachFunction");
        Function afterFunction = mock(Function.class, "afterFunction");

        doThrow(new RuntimeException("Before each failed")).when(beforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                beforeEach(beforeEachFunction);
                after(afterFunction);
                it("a test", () -> {
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(afterFunction).apply();
    }

    @Test
    public void shouldSkipAfterEachRunIfBeforeEachHookThrowsException() {

        //Given
        Function beforeEachFunction = mock(Function.class, "beforeEachFunction");
        Function afterEachFunction = mock(Function.class, "afterEachFunction");

        doThrow(new RuntimeException("Before each failed")).when(beforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                beforeEach(beforeEachFunction);
                afterEach(afterEachFunction);
                it("a test", () -> {
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(afterEachFunction).apply();
    }

    @Test
    public void shouldSkipTestsIfBeforeEachHookThrowsException() {

        //Given
        Function beforeEachFunction = mock(Function.class, "beforeEachFunction");
        Function testFunction1 = mock(Function.class, "testFunction");
        Function testFunction2 = mock(Function.class, "testFunction");

        doThrow(new RuntimeException("Before each failed")).when(beforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                beforeEach(beforeEachFunction);
                it("a test", testFunction1);
                it("a second test", testFunction2);
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(testFunction1, never()).apply();
        verify(testFunction2, never()).apply();
    }

    @Test
    public void shouldSkipNestedBeforeEachAfterEachAndTestsIfBeforeEachThrowsException() {

        //Given
        Function topLevelBeforeEachFunction = mock(Function.class, "topLevelBeforeEachFunction");
        Function nestedBeforeEachFunction = mock(Function.class, "nestedBeforeEachFunction");
        Function nestedAfterEachFunction = mock(Function.class, "nestedAfterEachFunction");
        Function nestedTestFunction = mock(Function.class, "nestedTestFunction");

        doThrow(new RuntimeException("Before each failed")).when(topLevelBeforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                beforeEach(topLevelBeforeEachFunction);
                when("nested block", () -> {
                    beforeEach(nestedBeforeEachFunction);
                    afterEach(nestedAfterEachFunction);
                    it("doesn't run the test nested", nestedTestFunction);
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(nestedBeforeEachFunction, never()).apply();
        verify(nestedAfterEachFunction, never()).apply();
        verify(nestedTestFunction, never()).apply();
    }

    @Test
    public void shouldRunNestedBeforeAndAfterHooksIfBeforeEachThrowsException() {

        //Given
        Function topLevelBeforeEachFunction = mock(Function.class, "topLevelBeforeEachFunction");
        Function nestedBeforeFunction = mock(Function.class, "nestedBeforeFunction");
        Function nestedAfterFunction = mock(Function.class, "nestedAfterFunction");

        doThrow(new RuntimeException("Before each failed")).when(topLevelBeforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                beforeEach(topLevelBeforeEachFunction);
                when("nested block", () -> {
                    before(nestedBeforeFunction);
                    after(nestedAfterFunction);
                    it("doesn't run the test nested", () -> {
                    });
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(nestedBeforeFunction).apply();
        verify(nestedAfterFunction).apply();
    }

    @Test
    public void shouldSkipAllNestedBlocksIfTopLevelBeforeEachThrowsException() {

        //Given
        Function topLevelBeforeEachFunction = mock(Function.class, "topLevelBeforeEachFunction");
        Function nestedBeforeFunction = mock(Function.class, "nestedBeforeFunction");

        doThrow(new RuntimeException("Before each failed")).when(topLevelBeforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                beforeEach(topLevelBeforeEachFunction);
                when("nested block", () -> {
                    it("doesn't run the test nested", () -> {
                    });
                });
                when("nested block", () -> {
                    before(nestedBeforeFunction);
                    it("doesn't run the test nested", () -> {
                    });
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(nestedBeforeFunction, never()).apply();
    }

    @Test
    public void shouldHandleDoubleNestedBlocksIfTopLevelBeforeEachThrowsException() {

        //Given
        Function topLevelBeforeEachFunction = mock(Function.class, "topLevelBeforeEachFunction");
        Function nestedBeforeFunction = mock(Function.class, "nestedBeforeFunction");

        doThrow(new RuntimeException("Before each failed")).when(topLevelBeforeEachFunction).apply();

        {
            describe("beforeEach blocks", () -> {
                when("nested block", () -> {
                    beforeEach(topLevelBeforeEachFunction);
                    when("double nested block", () -> {
                        it("doesn't run the test nested", () -> {
                        });
                    });
                });
                when("nested block 2", () -> {
                    before(nestedBeforeFunction);
                    it("doesn't run the test nested", () -> {
                    });
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(nestedBeforeFunction).apply();
    }

    @Test(enabled = false)
    public void shouldReturnAdditionalErrorResultIfAfterEachHookThrowsException() {
    }

    @Test
    public void shouldSkipRemainingTestsIfAfterEachThrowsException() {

        // Given
        Function afterEachFunction = mock(Function.class, "afterEachFunction");
        Function testFunction1 = mock(Function.class, "testFunction1");
        Function testFunction2 = mock(Function.class, "testFunction2");

        doThrow(new RuntimeException("After each failed")).when(afterEachFunction).apply();

        {
            describe("afterEach", () -> {
                afterEach(afterEachFunction);
                it("1", testFunction1);
                it("2", testFunction2);
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(testFunction2, never()).apply();
    }

    @Test
    public void shouldSkipRemainingBlocksIfAfterEachThrowsException() {

        // Given
        Function afterEachFunction = mock(Function.class, "afterEachFunction");
        Function testFunction1 = mock(Function.class, "testFunction1");
        Function testFunction2 = mock(Function.class, "testFunction2");

        doThrow(new RuntimeException("After each failed")).when(afterEachFunction).apply();

        {
            describe("afterEach", () -> {
                afterEach(afterEachFunction);
                when("nested", () -> {
                    it("1", testFunction1);
                });
                when("nested 2", () -> {
                    it("2", testFunction2);
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(testFunction2, never()).apply();
    }

    @Test
    public void shouldRunRemainingBlocksInOuterScopeIfNestedAfterEachThrowsException() {

        // Given
        Function afterEachFunction = mock(Function.class, "afterEachFunction");
        Function testFunction1 = mock(Function.class, "testFunction1");
        Function testFunction2 = mock(Function.class, "testFunction2");

        doThrow(new RuntimeException("After each failed")).when(afterEachFunction).apply();

        {
            describe("afterEach", () -> {
                when("nested", () -> {
                    afterEach(afterEachFunction);
                    it("1", testFunction1);
                });
                when("nested 2", () -> {
                    it("2", testFunction2);
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(testFunction2).apply();
    }

    @Test
    public void shouldRunRemainingBlocksInOuterScopeIfNestedAfterThrowsException() {

        // Given
        Function afterFunction = mock(Function.class, "afterFunction");
        Function testFunction1 = mock(Function.class, "testFunction1");
        Function testFunction2 = mock(Function.class, "testFunction2");

        doThrow(new RuntimeException("After failed")).when(afterFunction).apply();

        {
            describe("after", () -> {
                when("nested", () -> {
                    after(afterFunction);
                    it("1", testFunction1);
                });
                when("nested 2", () -> {
                    it("2", testFunction2);
                });
            });
        }

        //When
        Cuppa.runTests();

        //Then
        verify(testFunction2).apply();
    }
}