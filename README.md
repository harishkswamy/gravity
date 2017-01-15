#gravity

This is a dependency and configuration management framework that allows multiple forms of configuration via a plugin model with a primary focus on the Dependency Injection / Inversion of Control (IoC) design pattern. The framework comes with a default BeanShell plugin for configuring the components.

This is a proxy-based solution that allows constructor-injection, method-injection and combo-injection (a combination of constructor- and method-injection techniques).

This is not an AOP solution and hence there is no built-in solution for interceptors or mixins; there are lots of other good frameworks for this. However, there is a hook to allow proxy-based AOP solutions (like dynaop) to be plugged-in, to weave the components.

#Status - beta - 26-May-2004

The first beta version has been released. There is now a new manual, please see the documentation section. The next beta will primarily have improved test suite, better code coverage, bug fixes and minor feature enhancements.

#Next Steps

- Check out the [features](https://harishkswamy.github.io/gravity/features.html)
- Browse the [documentation](https://harishkswamy.github.io/gravity/doc.html); read the [manual](https://harishkswamy.github.io/gravity/framework/docs/manual/index.html)
- Try the framework; download the distribution and code away
- Get involved; take a peek at the source, submit bugs and/or patches, join the mailing list for discussion/support
