package zipfinder.testutil;

import java.lang.reflect.*;
import java.util.*;

/*
 * * This class is used to set protected fields in classes and access private
 * constructors.
 */
public final class ReflectionHelper {
	private static final String SETTER_PREFIX;
	private static final int SETTER_PREFIX_LENGTH;
	/**
	 * The object that contains the field.
	 */
	private final Object instance;
	static {
		SETTER_PREFIX = "set";
		SETTER_PREFIX_LENGTH = SETTER_PREFIX.length();
	}

	/**
	 * Instantiates a new reflection helper.
	 *
	 * @param instance the instance
	 */
	public ReflectionHelper(final Object instance) {
		this.instance = instance;
	}

	/**
	 * Instantiate the first constructor found and set dummy values directly on the fields.
	 * This method will try all constructors until it finds one that works.
	 *
	 * @param clazz the clazz to instantiate
	 *
	 * @return the new instance
	 */
	public static <T extends Object> T forceWithDummyValues(final Class<T> clazz) {
		T newInstance = HelperInstance.instantiateForced(clazz);
		new ReflectionHelper(newInstance).fillWithDummyFields();
		return newInstance;
	}

	/**
	 * Instantiate private empty constructor.
	 *
	 * @param clazz the clazz to instantiate
	 *
	 * @return the new instance
	 */
	public static <T> T instantiatePrivateConstructor(final Class<T> clazz) {
		return HelperInstance.instantiatePrivate(clazz);
	}

	/**
	 * Create new JavaBean with dummy values using setter methods.
	 *
	 * @param clazz the clazz to instantiate
	 *
	 * @return the new instance
	 */
	public static <T extends Object> T instantiateWithDummyValues(final Class<T> clazz) {
		T instance = HelperInstance.instantiate(clazz);
		new ReflectionHelper(instance).fillWithDummyProperties();
		return instance;
	}

	/**
	 * Fills the contained JavaBean, using fields directly, with dummy values
	 */
	public void fillWithDummyFields() {
		fillFields(this.instance);
	}

	/**
	 * Fills the contained JavaBean, using setter methods, with dummy values
	 */
	public void fillWithDummyProperties() {
		fillProperties(this.instance);
	}

	/**
	 * Gets the contined object of the ReflectionHelper.
	 *
	 * @return single instance of ReflectionHelper
	 */
	public Object getInstance() {
		return instance;
	}

	/**
	 * Sets a private or protected field for an object. This method uses type-matchning to set the field.
	 * This method can be used if a class only has one field of the specified type.
	 *
	 * @param fieldValue The value that the field should be set to.
	 * @throws IllegalAccessException Thrown if the field is final
	 */
	public void setField(final Object fieldValue) throws IllegalAccessException {
		Class<? extends Object> instanceClass = getInstance().getClass();
		Class<? extends Object> valueClass = fieldValue.getClass();
		Field[] fields = instanceClass.getDeclaredFields();
		List<Field> fieldMatches = new ArrayList<Field>();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(valueClass)) {
				fieldMatches.add(field);
			}
		}
		if (fieldMatches.size() == 0) {
			throw new IllegalArgumentException(String.format("Cannot find field for %s %s", valueClass, fieldValue));
		}
		if (fieldMatches.size() != 1) {
			throw new IllegalArgumentException(String.format("Found %s matches for field %s %s", fieldMatches.size(),
					valueClass, fieldValue));
		}
		Field field = fieldMatches.get(0);
		boolean memento = field.isAccessible();
		field.setAccessible(true);
		field.set(getInstance(), fieldValue);
		field.setAccessible(memento);
	}

	/**
	 * Sets a named private or protected field for an object. Use the type-matching setter if possible.
	 * This method can be used if a class only has more than one field of the specified type.
	 *
	 * @param fieldName The name of the field
	 * @param fieldValue The value that the field should be set to.
	 * @throws NoSuchFieldException Thrown if the fieldname is incorrect
	 * @throws IllegalAccessException Thrown if the field is final
	 */
	public void setField(final String fieldName, final Object fieldValue) throws NoSuchFieldException,
			IllegalAccessException {
		Class<? extends Object> clazz = getInstance().getClass();
		Field field = clazz.getDeclaredField(fieldName);
		boolean memento = field.isAccessible();
		field.setAccessible(true);
		field.set(getInstance(), fieldValue);
		field.setAccessible(memento);
	}

	private Object createDummyValue(final ParameterizedType genericParameterType, final Class<?> clazz,
			final String propertyName) {
		if (clazz.isArray()) {
			Class<?> componentType = clazz.getComponentType();
			Object array = Array.newInstance(componentType, 2);
			Array.set(array, 0, createDummyValue(null, componentType, propertyName + "0"));
			Array.set(array, 1, createDummyValue(null, componentType, propertyName + "1"));
			return array;
		}
		if (List.class == clazz) {
			Type[] actualTypeArguments = genericParameterType.getActualTypeArguments();
			if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?>) {
				List<Object> collection = new ArrayList<Object>();
				Class<?> componentType = (Class<?>) actualTypeArguments[0];
				Object value = createDummyValue(null, componentType, propertyName + "0");
				collection.add(value);
				value = createDummyValue(null, componentType, propertyName + "1");
				collection.add(value);
				return collection;
			}
			throw new RuntimeException(String.format("%s %s must use simple generics. Ie List<String>", clazz
					.getSimpleName(), genericParameterType));
		}
		if (Set.class == clazz) {
			Type[] actualTypeArguments = genericParameterType.getActualTypeArguments();
			if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?>) {
				Set<Object> collection = new TreeSet<Object>();
				Class<?> componentType = (Class<?>) actualTypeArguments[0];
				Object value = createDummyValue(null, componentType, propertyName + "0");
				collection.add(value);
				value = createDummyValue(null, componentType, propertyName + "1");
				collection.add(value);
				return collection;
			}
			throw new RuntimeException("List<???> must use simple generics. Ie List<String>");
		}
		if (clazz.isEnum()) {
			Object[] enumConstants = clazz.getEnumConstants();
			if (enumConstants.length == 0) {
				return null;
			}
			return enumConstants[0];
		}
		if (String.class == clazz) {
			return propertyName;
		}
		if (long.class == clazz || Long.class == clazz) {
			return (long) propertyName.hashCode();
		}
		if (double.class == clazz || Double.class == clazz) {
			return (double) propertyName.hashCode();
		}
		if (float.class == clazz || Float.class == clazz) {
			return (float) propertyName.hashCode();
		}
		if (int.class == clazz || Integer.class == clazz) {
			return (int) propertyName.hashCode();
		}
		if (char.class == clazz || Character.class == clazz) {
			return propertyName.charAt(0);
		}
		if (short.class == clazz || Short.class == clazz) {
			return (short) propertyName.hashCode();
		}
		if (Date.class == clazz) {
			return new Date(propertyName.hashCode());
		}
		if (boolean.class == clazz || Boolean.class == clazz) {
			return true;
		}
		return instantiateWithDummyValues(clazz);
	}

	private void fillFields(final Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			String propertyName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			Type type = field.getGenericType();
			Object value = createDummyValue(type instanceof ParameterizedType ? (ParameterizedType) type : null, field
					.getType(), propertyName);
			try {
				field.setAccessible(true);
				field.set(obj, value);
			} catch (Exception e) {
				// Silently ignore
			}
		}
	}

	private void fillProperties(final Object obj) {
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith(SETTER_PREFIX) && (method.getParameterTypes().length == 1)) {
				String propertyName = method.getName().substring(SETTER_PREFIX_LENGTH);
				Type type = method.getGenericParameterTypes()[0];
				Object value = createDummyValue(type instanceof ParameterizedType ? (ParameterizedType) type : null,
						method.getParameterTypes()[0], propertyName);
				try {
					method.invoke(obj, value);
				} catch (Exception e) {
					throw new RuntimeException(String.format("Method: %s, Value: %s", method.getName(), value), e);
				}
			}
		}
	}
}
