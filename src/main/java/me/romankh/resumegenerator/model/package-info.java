/**
 * @author Roman Khmelichek
 */
// Configure that fields and properties must be explicitly bound to XML via JAXB annotations.
// This annotation is inherited by all classes in this package.
@XmlAccessorType(XmlAccessType.NONE)
package me.romankh.resumegenerator.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;