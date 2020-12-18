#!/usr/bin/groovy

package githubwebapi;

def getGitHubPRInfos( github_token ) {
    branch_name = env.GIT_BRANCH
    if (env.BRANCH_NAME != null) {
        branch_name = env.BRANCH_NAME
    }

    pr_id = branch_name.substring( 3 )
    echo "PR ID : ${pr_id}"

    String url = "https://api.github.com/repos/FishingCactus/${env.PROJECT_NAME}/pulls/${pr_id}"
    
    def text = url.toURL().getText( requestProperties: [ 'Authorization' : "token ${github_token}" ] )
    def json = new JsonSlurper().parseText( text )

    log.info "PR Title : ${json.title}"

    return [ json.title, json.body, json.user.login, json.user.avatar_url ]
}

def getGitHubPRLastCommitterEmail( github_token ) {
    branch_name = env.GIT_BRANCH
    if (env.BRANCH_NAME != null) {
        branch_name = env.BRANCH_NAME
    }

    pr_id = branch_name.substring( 3 )

    String url = "https://api.github.com/repos/FishingCactus/${env.PROJECT_NAME}/pulls/${pr_id}/commits"
    
    def text = url.toURL().getText( requestProperties: [ 'Authorization' : "token ${github_token}" ] )
    def json = new JsonSlurper().parseText( text )
    def commits_count = json.size()
    def last_commit = json[ commits_count - 1 ]

    log.info "Last commit SHA : ${last_commit.sha}"

    def last_commit_author_email = last_commit.commit.author.email

    log.info "Last commit author email : ${last_commit_author_email}"

    return last_commit_author_email
}