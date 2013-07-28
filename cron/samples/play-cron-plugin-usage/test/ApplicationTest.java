import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import java.util.Set;

import org.junit.Test;

import play.mvc.Content;

import com.ssachtleben.play.plugin.cron.CronUtils;
import com.ssachtleben.play.plugin.cron.annotations.Cronjob;
import com.ssachtleben.play.plugin.cron.jobs.Job;
import com.ssachtleben.play.plugin.cron.usage.views.html.index;

/**
 * The ApplicationTest contains several JUnit test cases to keep safe that the
 * play-cron-plugin is working properly.
 * 
 * @author Sebastian Sachtleben
 */
public class ApplicationTest {

  /**
   * Check if both job search methods return the same results.
   */
  @Test
  public void findJobs() {
    Set<Job> annotatedClasses = CronUtils.findAnnotatedJobs(Cronjob.class);
    assertThat(annotatedClasses.size()).isGreaterThan(0);
    Set<Job> interfaceClasses = CronUtils.findImplementedJobs();
    assertThat(annotatedClasses.size()).isEqualTo(interfaceClasses.size());
  }

  /**
   * Check if the index view is rendered properly.
   */
  @Test
  public void renderIndexView() {
    Content html = index.render("The \"play-cron-plugin-usage\" application is up and running.");
    assertThat(contentType(html)).isEqualTo("text/html");
    assertThat(contentAsString(html)).contains("The \"play-cron-plugin-usage\" application is up and running.");
  }

}
