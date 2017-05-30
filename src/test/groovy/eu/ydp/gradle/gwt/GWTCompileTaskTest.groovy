package eu.ydp.gradle.gwt

import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class GWTCompileTaskTest {

	@Test
	public void canAddTasToProject() {
		Project project = ProjectBuilder.builder().build();
		def task = project.task('gwtCompile', type: GWTCompileTask)
		assertTrue(task instanceof GWTCompileTask)
	}
}