package dev.dong4j.zeka.kernel.common.util;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>Description: xpath解析xml</p>
 * <a href="http://www.w3school.com.cn/xpath/index.asp">...</a>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:27
 * @since 1.0.0
 */
public final class XmlUtil {
    /** preventedXXE */
    private static volatile boolean preventedXXE = false;
    /** Path */
    private final XPath path;
    /** Doc */
    private final Document doc;

    /**
     * Xml util
     *
     * @param inputSource input source
     * @throws ParserConfigurationException parser configuration exception
     * @throws SAXException                 sax exception
     * @throws IOException                  io exception
     * @since 1.0.0
     */
    private XmlUtil(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = getDocumentBuilderFactory();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(inputSource);
        path = getPathFactory().newXPath();
    }

    /**
     * Gets document builder factory *
     *
     * @return the document builder factory
     * @throws ParserConfigurationException parser configuration exception
     * @since 1.0.0
     */
    private static DocumentBuilderFactory getDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = XmlHelperHolder.DOCUMENT_BUILDER_FACTORY;
        if (!preventedXXE) {
            preventXxe(dbf);
        }
        return dbf;
    }

    /**
     * Gets x path factory *
     *
     * @return the x path factory
     * @since 1.0.0
     */
    @Contract(pure = true)
    private static XPathFactory getPathFactory() {
        return XmlHelperHolder.X_PATH_FACTORY;
    }

    /**
     * preventXxe
     *
     * @param dbf dbf
     * @throws ParserConfigurationException parser configuration exception
     * @since 1.0.0
     */
    private static void preventXxe(@NotNull DocumentBuilderFactory dbf) throws ParserConfigurationException {
        // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
        // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        // If you can't completely disable DTDs, then at least do the following:
        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities

        // JDK7+ - http://xml.org/sax/features/external-general-entities
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);

        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities

        // JDK7+ - http://xml.org/sax/features/external-parameter-entities
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        // Disable external DTDs as well
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        preventedXXE = true;
    }

    /**
     * Of xml util.
     *
     * @param is the is
     * @return the xml util
     * @since 1.0.0
     */
    @NotNull
    public static XmlUtil of(InputStream is) {
        InputSource inputSource = new InputSource(is);
        return create(inputSource);
    }

    /**
     * Create xml util
     *
     * @param inputSource input source
     * @return the xml util
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    private static XmlUtil create(InputSource inputSource) {
        try {
            return new XmlUtil(inputSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Of xml util.
     *
     * @param xmlStr the xml str
     * @return the xml util
     * @since 1.0.0
     */
    public static XmlUtil of(@NotNull String xmlStr) {
        StringReader sr = new StringReader(xmlStr.trim());
        InputSource inputSource = new InputSource(sr);
        XmlUtil xmlUtil = create(inputSource);
        IoUtils.closeQuietly(sr);
        return xmlUtil;
    }

    /**
     * 获取String
     *
     * @param expression 路径
     * @return String string
     * @since 1.0.0
     */
    public String getString(String expression) {
        return (String) evalPath(expression, null, XPathConstants.STRING);
    }

    /**
     * Eval x path object
     *
     * @param expression expression
     * @param item       item
     * @param returnType return type
     * @return the object
     * @since 1.0.0
     */
    private Object evalPath(String expression, @Nullable Object item, QName returnType) {
        item = null == item ? doc : item;
        try {
            return path.evaluate(expression, item, returnType);
        } catch (XPathExpressionException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 获取Boolean
     *
     * @param expression 路径
     * @return String boolean
     * @since 1.0.0
     */
    public Boolean getBoolean(String expression) {
        return (Boolean) evalPath(expression, null, XPathConstants.BOOLEAN);
    }

    /**
     * 获取Number
     *
     * @param expression 路径
     * @return {Number}
     * @since 1.0.0
     */
    public Number getNumber(String expression) {
        return (Number) evalPath(expression, null, XPathConstants.NUMBER);
    }

    /**
     * 获取某个节点
     *
     * @param expression 路径
     * @return {Node}
     * @since 1.0.0
     */
    public Node getNode(String expression) {
        return (Node) evalPath(expression, null, XPathConstants.NODE);
    }

    /**
     * 获取子节点
     *
     * @param expression 路径
     * @return NodeList node list
     * @since 1.0.0
     */
    public NodeList getNodeList(String expression) {
        return (NodeList) evalPath(expression, null, XPathConstants.NODESET);
    }

    /**
     * 获取String
     *
     * @param node       节点
     * @param expression 相对于node的路径
     * @return String string
     * @since 1.0.0
     */
    public String getString(Object node, String expression) {
        return (String) evalPath(expression, node, XPathConstants.STRING);
    }

    /**
     * 获取
     *
     * @param node       节点
     * @param expression 相对于node的路径
     * @return String boolean
     * @since 1.0.0
     */
    public Boolean getBoolean(Object node, String expression) {
        return (Boolean) evalPath(expression, node, XPathConstants.BOOLEAN);
    }

    /**
     * 获取
     *
     * @param node       节点
     * @param expression 相对于node的路径
     * @return {Number}
     * @since 1.0.0
     */
    public Number getNumber(Object node, String expression) {
        return (Number) evalPath(expression, node, XPathConstants.NUMBER);
    }

    /**
     * 获取某个节点
     *
     * @param node       节点
     * @param expression 路径
     * @return {Node}
     * @since 1.0.0
     */
    public Node getNode(Object node, String expression) {
        return (Node) evalPath(expression, node, XPathConstants.NODE);
    }

    /**
     * 获取子节点
     *
     * @param node       节点
     * @param expression 相对于node的路径
     * @return NodeList node list
     * @since 1.0.0
     */
    public NodeList getNodeList(Object node, String expression) {
        return (NodeList) evalPath(expression, node, XPathConstants.NODESET);
    }

    /**
     * 针对没有嵌套节点的简单处理
     *
     * @return map集合 map
     * @since 1.0.0
     */
    public Map<String, String> toMap() {
        Element root = doc.getDocumentElement();
        Map<String, String> params = Maps.newHashMap();

        // 将节点封装成map形式
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                params.put(node.getNodeName(), node.getTextContent());
            }
        }
        return params;
    }

    /**
     * 内部类单例
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 18:27
     * @since 1.0.0
     */
    private static class XmlHelperHolder {
        /** DOCUMENT_BUILDER_FACTORY */
        private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
        /** X_PATH_FACTORY */
        private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();
    }

}
