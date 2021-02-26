package org.laeq;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class TestService extends AbstractGriffonService {

}