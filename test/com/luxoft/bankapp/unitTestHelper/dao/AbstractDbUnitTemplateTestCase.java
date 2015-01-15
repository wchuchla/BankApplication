package com.luxoft.bankapp.unitTestHelper.dao;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;

public class AbstractDbUnitTemplateTestCase extends AbstractDbUnitTestCase {

    private static long id;

    public static class DataSetsTemplateRunner extends BlockJUnit4ClassRunner {

        public DataSetsTemplateRunner(Class<?> klass) throws InitializationError {
            super(klass);
        }

        @Override
        protected Statement methodInvoker(FrameworkMethod method, Object test) {
            return new AssertDataSetStatement(super.methodInvoker(method, test), method);
        }

        private void setupDataSet(FrameworkMethod method) {
            DataSets dataSetAnnotation = getAnnotation(method);
            String dataSetName = dataSetAnnotation.setUpDataSet();
            if (!dataSetName.equals("")) {
                try {
                    IDataSet dataSet = getReplacedDataSet(dataSetName, id);
                    DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, dataSet);
                } catch (Exception e) {
                    throw new RuntimeException("exception inserting dataset " + dataSetName, e);
                }
            }
        }

        private void assertDataSet(FrameworkMethod method) {
            DataSets dataSetAnnotation = getAnnotation(method);
            String dataSetName = dataSetAnnotation.assertDataSet();
            if (!dataSetName.equals("")) {
                try {
                    IDataSet expectedDataSet = getReplacedDataSet(dataSetName, id);
                    IDataSet actualDataSet = dbunitConnection.createDataSet();
                    Assertion.assertEquals(expectedDataSet, actualDataSet);
                } catch (Exception e) {
                    throw new RuntimeException("exception inserting dataset " + dataSetName, e);
                }
            }
        }

        private DataSets getAnnotation(FrameworkMethod method) {
            Method javaMethod = method.getMethod();
            return javaMethod.getAnnotation(DataSets.class);
        }

        private class AssertDataSetStatement extends Statement {

            private final Statement invoker;
            private final FrameworkMethod method;

            public AssertDataSetStatement(Statement invoker, FrameworkMethod method) {
                this.invoker = invoker;
                this.method = method;
            }

            @Override
            public void evaluate() throws Throwable {
                setupDataSet(method);
                invoker.evaluate();
                assertDataSet(method);
            }

        }

    }
}