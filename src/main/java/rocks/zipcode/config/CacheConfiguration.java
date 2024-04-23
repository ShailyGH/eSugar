package rocks.zipcode.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, rocks.zipcode.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, rocks.zipcode.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, rocks.zipcode.domain.User.class.getName());
            createCache(cm, rocks.zipcode.domain.Authority.class.getName());
            createCache(cm, rocks.zipcode.domain.User.class.getName() + ".authorities");
            createCache(cm, rocks.zipcode.domain.UserAccount.class.getName());
            createCache(cm, rocks.zipcode.domain.UserAccount.class.getName() + ".userProfiles");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName());
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".characteristicData");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".profileDashboardConfigs");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".bloodGlucoses");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".bodyVitalsLogs");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".userBMRS");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".activityLogs");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".bodyWeights");
            createCache(cm, rocks.zipcode.domain.UserProfile.class.getName() + ".bodyHeights");
            createCache(cm, rocks.zipcode.domain.CharacteristicData.class.getName());
            createCache(cm, rocks.zipcode.domain.ProfileDashboardConfig.class.getName());
            createCache(cm, rocks.zipcode.domain.BloodGlucose.class.getName());
            createCache(cm, rocks.zipcode.domain.BodyVitalsLog.class.getName());
            createCache(cm, rocks.zipcode.domain.BodyVitalsLog.class.getName() + ".dataSources");
            createCache(cm, rocks.zipcode.domain.BodyWeight.class.getName());
            createCache(cm, rocks.zipcode.domain.BodyHeight.class.getName());
            createCache(cm, rocks.zipcode.domain.DataSource.class.getName());
            createCache(cm, rocks.zipcode.domain.Activity.class.getName());
            createCache(cm, rocks.zipcode.domain.UserBMR.class.getName());
            createCache(cm, rocks.zipcode.domain.ActivityLog.class.getName());
            createCache(cm, rocks.zipcode.domain.ActivityLog.class.getName() + ".activities");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
