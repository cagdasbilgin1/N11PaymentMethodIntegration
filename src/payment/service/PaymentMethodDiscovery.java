package payment.service;

import payment.method.PaymentMethod;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class PaymentMethodDiscovery {

    private final ClassLoader classLoader;

    public PaymentMethodDiscovery() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public PaymentMethodDiscovery(ClassLoader classLoader) {
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader cannot be null");
    }

    public List<PaymentMethod> discover(String packageName) {
        Objects.requireNonNull(packageName, "packageName cannot be null");

        if (packageName.isBlank()) {
            throw new IllegalArgumentException("packageName cannot be blank");
        }

        List<PaymentMethod> paymentMethods = findClasses(packageName).stream()
                .filter(PaymentMethod.class::isAssignableFrom)
                .filter(type -> !type.isInterface())
                .filter(type -> !Modifier.isAbstract(type.getModifiers()))
                .sorted(Comparator.comparing(Class::getName))
                .map(this::createPaymentMethod)
                .toList();

        if (paymentMethods.isEmpty()) {
            throw new IllegalStateException("No payment methods found in package: " + packageName);
        }

        return paymentMethods;
    }

    private List<Class<?>> findClasses(String packageName) {
        String packagePath = packageName.replace('.', '/');
        List<Class<?>> classes = new ArrayList<>();

        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();

                if ("file".equals(resource.getProtocol())) {
                    classes.addAll(loadClassesFromDirectory(packageName, resource));
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not scan package: " + packageName, exception);
        }

        return classes;
    }

    private List<Class<?>> loadClassesFromDirectory(String packageName, URL packageResource) {
        try {
            Path directory = Path.of(packageResource.toURI());

            if (!Files.isDirectory(directory)) {
                return List.of();
            }

            try (Stream<Path> files = Files.list(directory)) {
                List<Class<?>> classes = new ArrayList<>();

                files
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(fileName -> fileName.endsWith(".class"))
                        .filter(fileName -> !fileName.contains("$"))
                        .map(fileName -> fileName.substring(0, fileName.length() - ".class".length()))
                        .map(className -> loadClass(packageName + "." + className))
                        .forEach(classes::add);

                return classes;
            }
        } catch (IOException | URISyntaxException exception) {
            throw new IllegalStateException("Could not load classes from package resource: " + packageResource, exception);
        }
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Could not load class: " + className, exception);
        }
    }

    private PaymentMethod createPaymentMethod(Class<?> paymentMethodClass) {
        try {
            return (PaymentMethod) paymentMethodClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not create payment method: " + paymentMethodClass.getName(), exception);
        }
    }
}
