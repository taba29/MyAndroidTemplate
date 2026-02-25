pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}



rootProject.name = "MyAndroidTemplate"

include(":app-commentviewer")
project(":app-commentviewer").projectDir = file("apps/app-commentviewer")

include(":feature-commentviewer")
project(":feature-commentviewer").projectDir = file("feature/commentviewer/feature-commentviewer")

include(":core-ui")
project(":core-ui").projectDir = file("core/ui/core-ui")

include(":core-data")
project(":core-data").projectDir = file("core/data/core-data")

include(":core-network")
project(":core-network").projectDir = file("core/network/core-network")

include(":core-storage")
project(":core-storage").projectDir = file("core/storage/core-storage")