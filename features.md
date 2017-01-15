#Features

These are some of the features implemented in Gravity. Feel free to discuss new features in the mailing list.

- Serves and injects components and data: Gravity, as a container, is capable of serving components and static data in the form of lists and maps and capable of injecting pretty much anything.
- Constructor and method injection: Dependencies can be injected into components via any constructor or method of the injected component.
- Component instances follow a life cycle: The phases of the life cycle are - instantiation, injection, start-up and shutdown. Callbacks can be registered for the injection and start-up phases of the life cycle.
- The life of the component instance is configurable: Depending on the strategy used by the component, the life of its instances will vary. The currently available strategies are - Singleton, Pooling and Thread Local.
- The life of the component instances can be changed dynamically: When the strategy of the component is changed, the life of all instances of the component, irrespective of when the instance was obtained, will be changed to obey the new strategy.
- Allows multi-faceted components: A component can act as a facade to another component there by allowing multiple views of the same component.
- Component configuration may be of distributed nature: Component configurations can be gathered from anywhere in the classpath.
- Component configuration is plugable: Components can be configured via any of the several possible ways - pure Java, BeanShell, XML etc. BeanShell is the framework default.
- Line precise error reporting: Runtime errors, when relevant, lead back to the exact line number on the configuration file.
