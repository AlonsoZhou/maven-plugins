package org.apache.maven.ant.tasks;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import org.apache.maven.plugin.antrun.AntRunMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.codehaus.plexus.util.FileUtils;

public class AttachArtifactTask
    extends Task
{

    /**
     * The refId of the maven project.
     */
    private String mavenProjectRefId = AntRunMojo.DEFAULT_MAVEN_PROJECT_REFID;

    /**
     * The refId of the maven project helper component.
     */
    private String mavenProjectHelperRefId = AntRunMojo.DEFAULT_MAVEN_PROJECT_HELPER_REFID;

    /**
     * The file to attach.
     */
    private File file;
    
    /**
     * The classifier of the artifact to attach
     */
    private String classifier;
    
    /**
     * The type of the artifact to attach.  Defaults to file extension.
     */
    private String type;
    
    public void execute()
    {
        if ( file == null )
        {
            throw new BuildException( "File is a required parameter." );
        }
        
        if ( !file.exists() )
        {
            throw new BuildException( "File does not exist: " + file );
        }
        
        if ( this.getProject().getReference( mavenProjectRefId ) == null )
        {
            throw new BuildException( "Maven project reference not found: " + mavenProjectRefId );
        }

        if ( type == null )
        {
            type = FileUtils.getExtension( file.getName() );
        }
        
        MavenProject mavenProject = (MavenProject) this.getProject().getReference( mavenProjectRefId );

        if ( classifier == null )
        {
            mavenProject.getArtifact().setFile( file );
        }
        else
        {
            if ( this.getProject().getReference( mavenProjectHelperRefId ) == null )
            {
                throw new BuildException( "Maven project helper reference not found: " + mavenProjectHelperRefId );
            }

            log( "Attaching " + file + " as an attached artifact", Project.MSG_VERBOSE );
            MavenProjectHelper projectHelper = (MavenProjectHelper) getProject().getReference( mavenProjectHelperRefId );
            projectHelper.attachArtifact( mavenProject, type, classifier, file );
        }
    }

    public File getFile()
    {
        return file;
    }

    public void setFile( File file )
    {
        this.file = file;
    }

    public String getMavenProjectRefId()
    {
        return mavenProjectRefId;
    }

    public void setMavenProjectRefId( String mavenProjectRefId )
    {
        this.mavenProjectRefId = mavenProjectRefId;
    }

    public String getClassifier()
    {
        return classifier;
    }

    public void setClassifier( String classifier )
    {
        this.classifier = classifier;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }
}
