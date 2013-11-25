package es.zaldo.petstore.core.dao.mixed;

import org.jboss.byteman.contrib.bmunit.BMUnitRunner;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Rule to be able to launch Byteman from Spring unit tests.
 */
public class BytemanRule extends BMUnitRunner implements MethodRule {

    /**
     *
     * @param klass
     * @return
     */
    public static BytemanRule create( Class< ? > klass ) {
        try {
            return new BytemanRule( klass );
        } catch( InitializationError ex ) {
            throw new RuntimeException( ex );
        }
    }

    /**
     *
     * @param klass
     * @throws InitializationError
     */
    private BytemanRule( Class<?> klass ) throws InitializationError {
        super( klass );
    }

    /**
     *
     */
    @Override
    public Statement apply( final Statement statement, final FrameworkMethod method, final Object target ) {
        Statement result = addMethodMultiRuleLoader( statement, method );

        if( result == statement ) {
            result = addMethodSingleRuleLoader( statement, method );
        }

        return result;
    }
}
