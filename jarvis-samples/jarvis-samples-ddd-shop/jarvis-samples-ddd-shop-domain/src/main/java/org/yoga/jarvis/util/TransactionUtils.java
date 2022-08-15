/*
 *  Copyright 2022 yoga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.yoga.jarvis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.yoga.jarvis.exception.JarvisException;

import java.util.concurrent.Callable;

/**
 * @Description: 事务工具类
 * @Author: yoga
 * @Date: 2022/5/11 10:00
 */
@Slf4j
@Component
public class TransactionUtils {

    private final DataSourceTransactionManager dataSourceTransactionManager;

    public TransactionUtils(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    /**
     * 执行事务
     *
     * @param job      需要在事务里执行的任务
     * @param rollback 回滚操作
     * @param <T>      泛型
     * @return 任务操作结果
     */
    public <T> T doTransaction(Callable<T> job, Runnable rollback) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 设置传播机制
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 事务状态
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // 执行任务
            T t = job.call();
            // 提交事务
            dataSourceTransactionManager.commit(status);
            // 返回任务操作结果
            return t;
        } catch (Exception e) {
            // 回滚事务
            dataSourceTransactionManager.rollback(status);
            // 回滚操作
            if (rollback != null) {
                rollback.run();
            }
            // 处理异常
            log.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 执行事务
     *
     * @param job      需要在事务里执行的任务
     * @param rollback 回滚操作
     */
    public void doTransaction(Runnable job, Runnable rollback) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 设置传播机制
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 事务状态
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // 执行任务
            job.run();
            // 提交事务
            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            // 回滚事务
            dataSourceTransactionManager.rollback(status);
            // 回滚操作
            if (rollback != null) {
                rollback.run();
            }
            // 处理异常
            log.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 执行事务
     *
     * @param job 需要在事务里执行的任务
     * @param <T> 泛型
     * @return 任务操作结果
     */
    public <T> T doTransaction(Callable<T> job) {
        return doTransaction(job, null);
    }

    /**
     * 执行事务
     *
     * @param job 需要在事务里执行的任务
     */
    public void doTransaction(Runnable job) {
        doTransaction(job, null);
    }

    /**
     * 开始事务
     *
     * @return 事务状态
     */
    public TransactionStatus begin() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return dataSourceTransactionManager.getTransaction(def);
    }

    /**
     * 提交事务
     *
     * @param status 事务状态
     */
    public void commit(TransactionStatus status) {
        dataSourceTransactionManager.commit(status);
    }

    /**
     * 回滚事务
     *
     * @param status 事务状态
     */
    public void rollback(TransactionStatus status) {
        dataSourceTransactionManager.rollback(status);
    }

    /**
     * 判断当前线程是否处于事物中，是就等事物提交后再执行，否则直接执行
     *
     * @param runnable 需要执行的任务
     */
    public static void executeAfterCommit(Runnable runnable) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }
}
