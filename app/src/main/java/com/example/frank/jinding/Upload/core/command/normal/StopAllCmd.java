package com.example.frank.jinding.Upload.core.command.normal;

//import com.arialyy.aria.core.inf.AbsTaskEntity;

import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;

/**
 * Created by AriaL on 2017/6/13.
 * 停止所有任务的命令，并清空所有等待队列
 */
final class StopAllCmd<T extends AbsTaskEntity> extends AbsNormalCmd<T> {
  /**
   * @param targetName 产生任务的对象名
   */
  StopAllCmd(String targetName, T entity, int taskType) {
    super(targetName, entity, taskType);
  }

  @Override
  public void executeCmd() {
    stopAll();
  }
}
