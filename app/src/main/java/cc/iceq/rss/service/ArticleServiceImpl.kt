package cc.iceq.rss.service

import android.util.Log
import cc.iceq.rss.RssApplication
import cc.iceq.rss.dao.AppDatabase
import cc.iceq.rss.model.ArticleInfo
import cc.iceq.rss.model.Feed
import cc.iceq.rss.model.FeedDetail
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL

class ArticleServiceImpl : ArticleService {

    val feedDao = AppDatabase.getDatabase(RssApplication.context).feedDao();
    val feedDetailDao = AppDatabase.getDatabase(RssApplication.context).feedDetailDao();

    override fun findSyncFeedByUrl(url: String): SyndFeed? {
        if (url == null || "".equals(url)) {
            return null
        }
        Log.i("INFO", "url is $url")
        val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
        Log.i("INFO", "list is ${feed.title}")
        Log.d("INFO", "feed is ${feed}")
        return feed
    }


    override fun findFeedDetailById(feedId: Long): List<FeedDetail> {
        return feedDetailDao.findByFeedId(feedId)
    }

    override fun containsUrl(link: String?): Boolean {
        if (link.isNullOrBlank()) {
            return true;
        } else {
            val list = feedDetailDao.findByUrl(link)
            if (list.isEmpty()) {
                return false;
            }else {
                return true
            }
        }
    }


    override fun insert(feed: Feed): Long {
        return feedDao.insertFeed(feed)
    }

    override fun insert(feedDetail: FeedDetail): Long {
        return feedDetailDao.insert(feedDetail)
    }

    override fun queryUrlById(itemId: Long): String {
        val findByItemId = feedDao.findByItemId(itemId)
        if (findByItemId.isNullOrEmpty()) {
            return ""
        }
        return findByItemId.get(0).url
    }

    override fun queryAll(): ArrayList<ArticleInfo> {
        val list = ArrayList<ArticleInfo>();
        val findAll = feedDao.findAll()

        findAll.forEach { item ->
            val name = item.name
            val url = item.url
            val author = item.author

            val articleInfo = ArticleInfo(item.id);
            articleInfo.title = name;
            articleInfo.url = url
            articleInfo.author = author
            articleInfo.desc = name
            list.add(articleInfo)
        }

        return list;
    }
}




