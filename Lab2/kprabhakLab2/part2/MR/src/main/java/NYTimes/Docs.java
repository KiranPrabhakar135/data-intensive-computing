package NYTimes;

import java.util.ArrayList;

public class Docs {
    private String web_url;
    private String snippet;
    private String lead_paragraph;
    private String _abstract;
    Blog BlogObject;
    private String source;
    Headline HeadlineObject;
    // ArrayList<Object> keywords = new ArrayList<Object>();
    private String pub_date;
    private String document_type;
    private String news_desk;
    private String section_name;
    private String subsection_name;
    Byline BylineObject;
    private String type_of_material;
    private String _id;
    private float word_count;
    private String uri;


    // Getter Methods

    public String getWeb_url() {
        return web_url;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getLead_paragraph() {
        return lead_paragraph;
    }

    public String getAbstract() {
        return _abstract;
    }

    public Blog getBlog() {
        return BlogObject;
    }

    public String getSource() {
        return source;
    }

    public Headline getHeadline() {
        return HeadlineObject;
    }

    public String getPub_date() {
        return pub_date;
    }

    public String getDocument_type() {
        return document_type;
    }

    public String getNews_desk() {
        return news_desk;
    }

    public String getSection_name() {
        return section_name;
    }

    public String getSubsection_name() {
        return subsection_name;
    }

    public Byline getByline() {
        return BylineObject;
    }

    public String getType_of_material() {
        return type_of_material;
    }

    public String get_id() {
        return _id;
    }

    public float getWord_count() {
        return word_count;
    }

    public String getUri() {
        return uri;
    }

    // Setter Methods

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setLead_paragraph(String lead_paragraph) {
        this.lead_paragraph = lead_paragraph;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    public void setBlog(Blog blogObject) {
        this.BlogObject = blogObject;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setHeadline(Headline headlineObject) {
        this.HeadlineObject = headlineObject;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public void setNews_desk(String news_desk) {
        this.news_desk = news_desk;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public void setSubsection_name(String subsection_name) {
        this.subsection_name = subsection_name;
    }

    public void setByline(Byline bylineObject) {
        this.BylineObject = bylineObject;
    }

    public void setType_of_material(String type_of_material) {
        this.type_of_material = type_of_material;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setWord_count(float word_count) {
        this.word_count = word_count;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
class Byline {
    private String original;
    ArrayList < Object > person = new ArrayList < Object > ();
    private String organization = null;


    // Getter Methods

    public String getOriginal() {
        return original;
    }

    public String getOrganization() {
        return organization;
    }

    // Setter Methods

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
class Headline {
    private String main;
    private String kicker = null;
    private String content_kicker = null;
    private String print_headline;
    private String name = null;
    private String seo = null;
    private String sub = null;


    // Getter Methods

    public String getMain() {
        return main;
    }

    public String getKicker() {
        return kicker;
    }

    public String getContent_kicker() {
        return content_kicker;
    }

    public String getPrint_headline() {
        return print_headline;
    }

    public String getName() {
        return name;
    }

    public String getSeo() {
        return seo;
    }

    public String getSub() {
        return sub;
    }

    // Setter Methods

    public void setMain(String main) {
        this.main = main;
    }

    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

    public void setContent_kicker(String content_kicker) {
        this.content_kicker = content_kicker;
    }

    public void setPrint_headline(String print_headline) {
        this.print_headline = print_headline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
class Blog {
}
