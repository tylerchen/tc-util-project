import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;

import org.iff.infra.util.MapHelper;
import org.iff.infra.util.freemarker.FreeMarkerConfiguration;
import org.iff.infra.util.freemarker.model.MvelDirective;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
public class TestFreemarkerDirective {
	private static final BeansWrapper beansWrapper = new freemarker.ext.beans.BeansWrapperBuilder(
			freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
	private static final freemarker.template.TemplateModel helper = beansWrapper.getStaticModels();

	public static void main3(String[] args) {
		try {
			FreeMarkerConfiguration config = new FreeMarkerConfiguration();
			config.setDirectoryForTemplateLoading(
					new File("/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl"));

			config.setDirectivePath("org.iff.infra.util.freemarker.model");

			Template template = config.getTemplate("test.ftl", "UTF-8");
			template.process(MapHelper.toMap("test", "123"), new PrintWriter(System.out));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main5(String[] args) {
		try {
			FreeMarkerConfiguration config = new FreeMarkerConfiguration();
			config.setDirectoryForTemplateLoading(
					new File("/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl"));

			System.out.println(config.getDefaultEncoding());
			System.out.println(config.getOutputEncoding());

			config.setDirectivePath("org.iff.infra.util.freemarker.model");

			Template template = config.getTemplate("test_file_output.ftl", "UTF-8");
			template.process(MapHelper.toMap("test", "123"), new PrintWriter(System.out));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main2(String[] args) {
		try {
			FreeMarkerConfiguration config = new FreeMarkerConfiguration();
			config.setDirectoryForTemplateLoading(
					new File("/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl"));

			config.setDirectivePath("org.iff.infra.util.freemarker.model");

			Template template = config.getTemplate("mvel.ftl", "UTF-8");
			template.process(MapHelper.toMap("test", "123"), new PrintWriter(System.out));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main1(String[] args) {
		try {
			Configuration config = new freemarker.template.Configuration(
					freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
			//设置要解析的模板所在的目录，并加载模板文件  
			config.setDirectoryForTemplateLoading(
					new File("/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl"));
			config.setTemplateUpdateDelayMilliseconds(0);
			config.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);

			config.setObjectWrapper(beansWrapper);
			config.setDefaultEncoding("UTF-8");
			config.setOutputEncoding("UTF-8");
			config.setLocale(java.util.Locale.CHINA);
			config.setLocalizedLookup(false);
			config.setNumberFormat("#0.#####");
			config.setDateFormat("yyyy-MM-dd");
			config.setTimeFormat("HH:mm:ss");
			config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");

			config.setSharedVariable("mvel", new MvelDirective());

			//获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致  
			Template template = config.getTemplate("mvel.ftl", "UTF-8");
			//合并数据模型与模板  
			template.process(MapHelper.toMap("test", "123"), new PrintWriter(System.out));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			FreeMarkerConfiguration config = new FreeMarkerConfiguration();
			config.setDirectoryForTemplateLoading(
					new File("/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl"));

			config.setDirectivePath("org.iff.infra.util.freemarker.model");

			Template template = config.getTemplate("test_groovy.ftl", "UTF-8");
			template.process(MapHelper.toMap("test", "123"), new PrintWriter(System.out));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
