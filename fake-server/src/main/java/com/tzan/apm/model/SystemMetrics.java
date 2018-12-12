package com.tzan.apm.model;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-12
 */
public class SystemMetrics {

  private String applicationName;
  private String applicationId;
  private int instanceIndex;
  private double cpuPercentage;
  private long memoryBytes;
  private long diskBytes;
  private long memoryBytesQuota;
  private long diskBytesQuota;

  public SystemMetrics() {
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public int getInstanceIndex() {
    return instanceIndex;
  }

  public void setInstanceIndex(int instanceIndex) {
    this.instanceIndex = instanceIndex;
  }

  public double getCpuPercentage() {
    return cpuPercentage;
  }

  public void setCpuPercentage(double cpuPercentage) {
    this.cpuPercentage = cpuPercentage;
  }

  public long getMemoryBytes() {
    return memoryBytes;
  }

  public void setMemoryBytes(long memoryBytes) {
    this.memoryBytes = memoryBytes;
  }

  public long getDiskBytes() {
    return diskBytes;
  }

  public void setDiskBytes(long diskBytes) {
    this.diskBytes = diskBytes;
  }

  public long getMemoryBytesQuota() {
    return memoryBytesQuota;
  }

  public void setMemoryBytesQuota(long memoryBytesQuota) {
    this.memoryBytesQuota = memoryBytesQuota;
  }

  public long getDiskBytesQuota() {
    return diskBytesQuota;
  }

  public void setDiskBytesQuota(long diskBytesQuota) {
    this.diskBytesQuota = diskBytesQuota;
  }
}
