package sample.app.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

public class CustomMatchers {
    private static class SubClassesOf extends AbstractMatcher<TypeLiteral<?>> {
        private final Class<?> baseClass;

        private SubClassesOf(Class<?> baseClass) {
            this.baseClass = baseClass;
        }

        @Override
        public boolean matches(TypeLiteral<?> t) {
            return baseClass.isAssignableFrom(t.getRawType());
        }
    }

    private static class AnnotatedWith extends AbstractMatcher<TypeLiteral<?>> {
        private final Class<? extends Annotation> baseClass;

        private AnnotatedWith(Class<? extends Annotation> baseClass) {
            this.baseClass = baseClass;
        }

        @Override
        public boolean matches(TypeLiteral<?> t) {
            try {
                return t.getRawType().isAnnotationPresent(baseClass);
            } catch (final Exception e) {
                return false;
            }
        }
    }

    private static class MethodMatcher extends AbstractMatcher<Method> {
        private final String methodName;

        private MethodMatcher(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public boolean matches(Method t) {
            return t.getName().equals(methodName);
        }
    }

    /**
     * Matcher matches all classes that extends, implements or is the same as baseClass
     *
     * @param baseClass
     * @return Matcher
     */
    public static Matcher<TypeLiteral<?>> subclassesOf(Class<?> baseClass) {
        return new SubClassesOf(baseClass);
    }

    public static Matcher<TypeLiteral<?>> annotatedWith(Class<? extends Annotation> aClass) {
        return new AnnotatedWith(aClass);
    }

    public static Matcher<Method> matchesMethod(String methodName) {
        return new MethodMatcher(methodName);
    }
}
