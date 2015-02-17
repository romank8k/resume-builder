package me.romankh.resumegenerator.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Roman Khmelichek
 */
@XmlRootElement(name = "resume")
public class Resume {
  @XmlElement(name = "meta")
  private ResumeMeta resumeMeta;

  @XmlElement(name = "header")
  private ResumeHeader resumeHeader;

  @XmlElement(name = "content")
  private Content content;

  public ResumeMeta getResumeMeta() {
    return resumeMeta;
  }

  public Resume setResumeMeta(ResumeMeta resumeMeta) {
    this.resumeMeta = resumeMeta;
    return this;
  }

  public ResumeHeader getResumeHeader() {
    return resumeHeader;
  }

  public Resume setResumeHeader(ResumeHeader resumeHeader) {
    this.resumeHeader = resumeHeader;
    return this;
  }

  public Content getContent() {
    return content;
  }

  public Resume setContent(Content content) {
    this.content = content;
    return this;
  }
}
