package ayds.songinfo.home.view

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseDateResolverFactoryImplTest {

    private val releaseDateResolverFactory: ReleaseDateResolverFactory =
        ReleaseDateResolverFactoryImpl()

    @Test
    fun `on day precision should return day precision resolver`() {
        val releaseDateResolverFactory: ReleaseDateResolverFactory =
            ReleaseDateResolverFactoryImpl()

        val result = releaseDateResolverFactory.get(mockk {
            every { releaseDatePrecision } returns "day"
        })


        assertEquals(result::class.java, ReleaseDateDayResolverImpl::class.java)
    }

    @Test
    fun `on month precision should return month precision resolver`() {
        val result = releaseDateResolverFactory.get(mockk {
            every { releaseDatePrecision } returns "month"
        })

        assertEquals(result::class.java, ReleaseDateMonthResolverImpl::class.java)
    }

    @Test
    fun `on year precision should return year precision resolver`() {
        val result = releaseDateResolverFactory.get(mockk {
            every { releaseDatePrecision } returns "year"
        })

        assertEquals(result::class.java, ReleaseDateYearResolverImpl::class.java)
    }

}