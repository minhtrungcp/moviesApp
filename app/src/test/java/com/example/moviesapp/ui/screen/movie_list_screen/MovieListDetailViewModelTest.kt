package com.example.moviesapp.ui.screen.movie_list_screen

import android.app.Application
import com.example.moviesapp.R
import com.example.moviesapp.core.MainCoroutineRule
import com.example.moviesapp.core.Result
import com.example.moviesapp.data.model.config.dao.ImageConfigurationDAO
import com.example.moviesapp.data.model.movie.list.MovieResponseDTO
import com.example.moviesapp.data.model.movie.list.toMovieList
import com.example.moviesapp.domain.model.movie.list.MovieList
import com.example.moviesapp.domain.use_case.GetMoviesUseCase
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieListDetailViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var imageConfigurationDAO: ImageConfigurationDAO

    @Mock
    private lateinit var getMoviesUseCase: GetMoviesUseCase

    lateinit var viewModel: MovieListDetailViewModel

    val movieListResponnse =
        "{\"dates\":{\"maximum\":\"2022-03-14\",\"minimum\":\"2022-01-25\"},\"page\":1,\"results\":[{\"adult\":false,\"backdrop_path\":\"/fOy2Jurz9k6RnJnMUMRDAgBwru2.jpg\",\"genre_ids\":[16,10751,35,14],\"id\":508947,\"original_language\":\"en\",\"original_title\":\"Turning Red\",\"overview\":\"Thirteen-year-old Mei is experiencing the awkwardness of being a teenager with a twist – when she gets too excited, she transforms into a giant red panda.\",\"popularity\":3909.877,\"poster_path\":\"/qsdjk9oAKSQMWs0Vt5Pyfh6O4GZ.jpg\",\"release_date\":\"2022-03-10\",\"title\":\"Turning Red\",\"video\":false,\"vote_average\":7.5,\"vote_count\":423},{\"adult\":false,\"backdrop_path\":\"/2hGjmgZrS1nlsEl5PZorn7EsmzH.jpg\",\"genre_ids\":[28,53],\"id\":823625,\"original_language\":\"en\",\"original_title\":\"Blacklight\",\"overview\":\"Travis Block is a shadowy Government agent who specializes in removing operatives whose covers have been exposed. He then has to uncover a deadly conspiracy within his own ranks that reaches the highest echelons of power.\",\"popularity\":3290.397,\"poster_path\":\"/woBl0cQCCDIVcpitBaul2dmqNjY.jpg\",\"release_date\":\"2022-02-10\",\"title\":\"Blacklight\",\"video\":false,\"vote_average\":5.5,\"vote_count\":134},{\"adult\":false,\"backdrop_path\":\"/5P8SmMzSNYikXpxil6BYzJ16611.jpg\",\"genre_ids\":[80,9648,53],\"id\":414906,\"original_language\":\"en\",\"original_title\":\"The Batman\",\"overview\":\"In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.\",\"popularity\":2799.005,\"poster_path\":\"/74xTEgt7R36Fpooo50r9T25onhq.jpg\",\"release_date\":\"2022-03-01\",\"title\":\"The Batman\",\"video\":false,\"vote_average\":8,\"vote_count\":1850},{\"adult\":false,\"backdrop_path\":\"/ifUfE79O1raUwbaQRIB7XnFz5ZC.jpg\",\"genre_ids\":[27,9648,53],\"id\":646385,\"original_language\":\"en\",\"original_title\":\"Scream\",\"overview\":\"Twenty-five years after a streak of brutal murders shocked the quiet town of Woodsboro, a new killer has donned the Ghostface mask and begins targeting a group of teenagers to resurrect secrets from the town’s deadly past.\",\"popularity\":1283.389,\"poster_path\":\"/kZNHR1upJKF3eTzdgl5V8s8a4C3.jpg\",\"release_date\":\"2022-01-12\",\"title\":\"Scream\",\"video\":false,\"vote_average\":6.8,\"vote_count\":916},{\"adult\":false,\"backdrop_path\":\"/cugmVwK0N4aAcLibelKN5jWDXSx.jpg\",\"genre_ids\":[16,28,14,12],\"id\":768744,\"original_language\":\"ja\",\"original_title\":\"僕のヒーローアカデミア THE MOVIE ワールド ヒーローズ ミッション\",\"overview\":\"A mysterious group called Humarize strongly believes in the Quirk Singularity Doomsday theory which states that when quirks get mixed further in with future generations, that power will bring forth the end of humanity. In order to save everyone, the Pro-Heroes around the world ask UA Academy heroes-in-training to assist them and form a world-classic selected hero team. It is up to the heroes to save the world and the future of heroes in what is the most dangerous crisis to take place yet in My Hero Academia.\",\"popularity\":1261.65,\"poster_path\":\"/4NUzcKtYPKkfTwKsLjwNt8nRIXV.jpg\",\"release_date\":\"2021-08-06\",\"title\":\"My Hero Academia: World Heroes' Mission\",\"video\":false,\"vote_average\":7.3,\"vote_count\":111},{\"adult\":false,\"backdrop_path\":\"/qBLEWvJNVsehJkEJqIigPsWyBse.jpg\",\"genre_ids\":[16,10751,14,35,12],\"id\":585083,\"original_language\":\"en\",\"original_title\":\"Hotel Transylvania: Transformania\",\"overview\":\"When Van Helsing's mysterious invention, the \\\"Monsterfication Ray,\\\" goes haywire, Drac and his monster pals are all transformed into humans, and Johnny becomes a monster. In their new mismatched bodies, Drac and Johnny must team up and race across the globe to find a cure before it's too late, and before they drive each other crazy.\",\"popularity\":1228.554,\"poster_path\":\"/teCy1egGQa0y8ULJvlrDHQKnxBL.jpg\",\"release_date\":\"2022-02-25\",\"title\":\"Hotel Transylvania: Transformania\",\"video\":false,\"vote_average\":7.1,\"vote_count\":394},{\"adult\":false,\"backdrop_path\":\"/g0YNGpmlXsgHfhGnJz3c5uyzZ1B.jpg\",\"genre_ids\":[80,18,53],\"id\":597208,\"original_language\":\"en\",\"original_title\":\"Nightmare Alley\",\"overview\":\"An ambitious carnival man with a talent for manipulating people with a few well-chosen words hooks up with a female psychiatrist who is even more dangerous than he is.\",\"popularity\":1154.069,\"poster_path\":\"/vfn1feL0V9HNSXuLLpaxAW8O6LO.jpg\",\"release_date\":\"2021-12-02\",\"title\":\"Nightmare Alley\",\"video\":false,\"vote_average\":7.1,\"vote_count\":1026},{\"adult\":false,\"backdrop_path\":\"/i5dUmY2xRzgkmjHJYKNj8kPX1Xx.jpg\",\"genre_ids\":[37,28],\"id\":928999,\"original_language\":\"en\",\"original_title\":\"Desperate Riders\",\"overview\":\"After Kansas Red rescues young Billy from a card-game shootout, the boy asks Red for help protecting his family from the outlaw Thorn, who’s just kidnapped Billy’s mother, Carol. As Red and Billy ride off to rescue Carol, they run into beautiful, tough-as-nails Leslie, who’s managed to escape Thorn’s men. The three race to stop Thorn’s wedding to Carol with guns a-blazing - but does she want to be rescued?\",\"popularity\":1141.188,\"poster_path\":\"/7pYYGm1dWZGkbJuhcuaHD6nE6k7.jpg\",\"release_date\":\"2022-02-25\",\"title\":\"Desperate Riders\",\"video\":false,\"vote_average\":6,\"vote_count\":17},{\"adult\":false,\"backdrop_path\":\"/mruT954ve6P1zquaRs6XG0hA5k9.jpg\",\"genre_ids\":[53],\"id\":800510,\"original_language\":\"en\",\"original_title\":\"Kimi\",\"overview\":\"A tech worker with agoraphobia discovers recorded evidence of a violent crime but is met with resistance when she tries to report it. Seeking justice, she must do the thing she fears the most: she must leave her apartment.\",\"popularity\":1114.247,\"poster_path\":\"/okNgwtxIWzGsNlR3GsOS0i0Qgbn.jpg\",\"release_date\":\"2022-02-10\",\"title\":\"Kimi\",\"video\":false,\"vote_average\":6.3,\"vote_count\":256},{\"adult\":false,\"backdrop_path\":\"/eVSa4TpyXbOdk9fXSD6OcORJGtv.jpg\",\"genre_ids\":[53],\"id\":803114,\"original_language\":\"en\",\"original_title\":\"The Requin\",\"overview\":\"A couple on a romantic getaway find themselves stranded at sea when a tropical storm sweeps away their villa. In order to survive, they are forced to fight the elements, while sharks circle below.\",\"popularity\":1021.576,\"poster_path\":\"/i0z8g2VRZP3dhVvvSMilbOZMKqR.jpg\",\"release_date\":\"2022-01-28\",\"title\":\"The Requin\",\"video\":false,\"vote_average\":4.8,\"vote_count\":78},{\"adult\":false,\"backdrop_path\":\"/tutaKitJJIaqZPyMz7rxrhb4Yxm.jpg\",\"genre_ids\":[16,35,10751,10402],\"id\":438695,\"original_language\":\"en\",\"original_title\":\"Sing 2\",\"overview\":\"Buster and his new cast now have their sights set on debuting a new show at the Crystal Tower Theater in glamorous Redshore City. But with no connections, he and his singers must sneak into the Crystal Entertainment offices, run by the ruthless wolf mogul Jimmy Crystal, where the gang pitches the ridiculous idea of casting the lion rock legend Clay Calloway in their show. Buster must embark on a quest to find the now-isolated Clay and persuade him to return to the stage.\",\"popularity\":1046.758,\"poster_path\":\"/aWeKITRFbbwY8txG5uCj4rMCfSP.jpg\",\"release_date\":\"2021-12-01\",\"title\":\"Sing 2\",\"video\":false,\"vote_average\":8.2,\"vote_count\":2249},{\"adult\":false,\"backdrop_path\":\"/yKnjIWNIVECfMoKy1ayl68vX6qj.jpg\",\"genre_ids\":[28,80,53],\"id\":871799,\"original_language\":\"en\",\"original_title\":\"Pursuit\",\"overview\":\"Detective Breslin crosses paths with Calloway, a ruthless hacker desperate to find his wife, who has been kidnapped by a drug cartel. When Calloway escapes police custody, Breslin joins forces with a no-nonsense female cop to reclaim his prisoner. But is Calloway’s crime-boss father somehow involved in this explosive situation?\",\"popularity\":1016.423,\"poster_path\":\"/oUU6blOmIu155vfIgzML3ZSsDrB.jpg\",\"release_date\":\"2022-02-18\",\"title\":\"Pursuit\",\"video\":false,\"vote_average\":5.7,\"vote_count\":23},{\"adult\":false,\"backdrop_path\":\"/ewUqXnwiRLhgmGhuksOdLgh49Ch.jpg\",\"genre_ids\":[878,12,35],\"id\":696806,\"original_language\":\"en\",\"original_title\":\"The Adam Project\",\"overview\":\"After accidentally crash-landing in 2022, time-traveling fighter pilot Adam Reed teams up with his 12-year-old self on a mission to save the future.\",\"popularity\":1090.761,\"poster_path\":\"/wFjboE0aFZNbVOF05fzrka9Fqyx.jpg\",\"release_date\":\"2022-03-11\",\"title\":\"The Adam Project\",\"video\":false,\"vote_average\":7.1,\"vote_count\":570},{\"adult\":false,\"backdrop_path\":\"/o76ZDm8PS9791XiuieNB93UZcRV.jpg\",\"genre_ids\":[27,28,878],\"id\":460458,\"original_language\":\"en\",\"original_title\":\"Resident Evil: Welcome to Raccoon City\",\"overview\":\"Once the booming home of pharmaceutical giant Umbrella Corporation, Raccoon City is now a dying Midwestern town. The company’s exodus left the city a wasteland…with great evil brewing below the surface. When that evil is unleashed, the townspeople are forever…changed…and a small group of survivors must work together to uncover the truth behind Umbrella and make it through the night.\",\"popularity\":831.683,\"poster_path\":\"/7uRbWOXxpWDMtnsd2PF3clu65jc.jpg\",\"release_date\":\"2021-11-24\",\"title\":\"Resident Evil: Welcome to Raccoon City\",\"video\":false,\"vote_average\":6.2,\"vote_count\":1340},{\"adult\":false,\"backdrop_path\":\"/pwpw1veVNW2Sls5sGbA9mqMYN49.jpg\",\"genre_ids\":[28,12,35,36],\"id\":591120,\"original_language\":\"ko\",\"original_title\":\"해적: 도깨비 깃발\",\"overview\":\"A gutsy crew of Joseon pirates and bandits battle stormy waters, puzzling clues and militant rivals in search of royal gold lost at sea.\",\"popularity\":782.767,\"poster_path\":\"/zAp8FqC4pLfqcsEfuHwQflCWsA5.jpg\",\"release_date\":\"2022-01-26\",\"title\":\"The Pirates: The Last Royal Treasure\",\"video\":false,\"vote_average\":6.6,\"vote_count\":55},{\"adult\":false,\"backdrop_path\":\"/cTTggc927lEPCMsWUsdugSj6wAY.jpg\",\"genre_ids\":[28,12],\"id\":335787,\"original_language\":\"en\",\"original_title\":\"Uncharted\",\"overview\":\"A young street-smart, Nathan Drake and his wisecracking partner Victor “Sully” Sullivan embark on a dangerous pursuit of “the greatest treasure never found” while also tracking clues that may lead to Nathan’s long-lost brother.\",\"popularity\":794.642,\"poster_path\":\"/sqLowacltbZLoCa4KYye64RvvdQ.jpg\",\"release_date\":\"2022-02-10\",\"title\":\"Uncharted\",\"video\":false,\"vote_average\":7.1,\"vote_count\":770},{\"adult\":false,\"backdrop_path\":\"/8pgKccb5PfE1kWB9qqiXJem83VC.jpg\",\"genre_ids\":[28,53],\"id\":522016,\"original_language\":\"en\",\"original_title\":\"The 355\",\"overview\":\"A group of top female agents from American, British, Chinese, Columbian and German  government agencies are drawn together to try and stop an organization from acquiring a deadly weapon to send the world into chaos.\",\"popularity\":718.056,\"poster_path\":\"/zxizwEPE8jhpbMgrFbwCztgvh2m.jpg\",\"release_date\":\"2022-01-05\",\"title\":\"The 355\",\"video\":false,\"vote_average\":6.1,\"vote_count\":328},{\"adult\":false,\"backdrop_path\":\"/ilty8eu65u6vCJpyMva9ele8Qtm.jpg\",\"genre_ids\":[10749,35,10402],\"id\":615904,\"original_language\":\"en\",\"original_title\":\"Marry Me\",\"overview\":\"Music superstars Kat Valdez and Bastian are getting married before a global audience of fans. But when Kat learns, seconds before her vows, that Bastian has been unfaithful, she decides to marry Charlie, a stranger in the crowd, instead.\",\"popularity\":673.661,\"poster_path\":\"/ko1JVbGj4bT8IhCWqjBQ6ZtF2t.jpg\",\"release_date\":\"2022-02-09\",\"title\":\"Marry Me\",\"video\":false,\"vote_average\":6.9,\"vote_count\":246},{\"adult\":false,\"backdrop_path\":\"/1Wlwnhn5sXUIwlxpJgWszT622PS.jpg\",\"genre_ids\":[10751,12,35,14],\"id\":585245,\"original_language\":\"en\",\"original_title\":\"Clifford the Big Red Dog\",\"overview\":\"As Emily struggles to fit in at home and at school, she discovers a small red puppy who is destined to become her best friend. When Clifford magically undergoes one heck of a growth spurt, becomes a gigantic dog and attracts the attention of a genetics company, Emily and her Uncle Casey have to fight the forces of greed as they go on the run across New York City. Along the way, Clifford affects the lives of everyone around him and teaches Emily and her uncle the true meaning of acceptance and unconditional love.\",\"popularity\":571.704,\"poster_path\":\"/oifhfVhUcuDjE61V5bS5dfShQrm.jpg\",\"release_date\":\"2021-11-10\",\"title\":\"Clifford the Big Red Dog\",\"video\":false,\"vote_average\":7.3,\"vote_count\":1116},{\"adult\":false,\"backdrop_path\":\"/yzH5zvuEzzsHLZnn0jwYoPf0CMT.jpg\",\"genre_ids\":[53,28],\"id\":760926,\"original_language\":\"en\",\"original_title\":\"Gold\",\"overview\":\"In the not-too-distant future, two drifters traveling through the desert stumble across the biggest gold nugget ever found and the dream of immense wealth and greed takes hold. They hatch a plan to excavate their bounty, with one man leaving to secure the necessary tools while the other remains with the gold. The man who remains must endure harsh desert elements, ravenous wild dogs, and mysterious intruders, while battling the sinking suspicion that he has been abandoned to his fate.\",\"popularity\":536.074,\"poster_path\":\"/ejXBuNLvK4kZ7YcqeKqUWnCxdJq.jpg\",\"release_date\":\"2022-01-13\",\"title\":\"Gold\",\"video\":false,\"vote_average\":6.6,\"vote_count\":120}],\"total_pages\":59,\"total_results\":1175}"

    @Before
    fun setUp() {
        viewModel = MovieListDetailViewModel(application, imageConfigurationDAO, getMoviesUseCase)
    }

    @Test
    fun getListMovies_Success() =
        runBlockingTest {
            val movieList =
                Gson().fromJson(movieListResponnse, MovieResponseDTO::class.java).toMovieList()
            val flow = flow {
                emit(Result.Loading<MovieList>())
                emit(Result.Success<MovieList>(movieList))
            }
            doReturn(flow)
                .`when`(getMoviesUseCase)
                .getListMovies("now_playing", 1)
            viewModel.path = "now_playing"
            assertEquals(20, viewModel.movies.value.size)
        }

    @Test
    fun getListMovies_Error() =
        runBlockingTest {
            val flow = flow {
                emit(Result.Loading<MovieList>())
                emit(Result.Error<MovieList>(application.getString(R.string.unknown_error)))
            }
            doReturn(flow)
                .`when`(getMoviesUseCase)
                .getListMovies("now_playing", 1)
            viewModel.path = "now_playing"
            assertEquals(0, viewModel.movies.value.size)
        }

    @After
    fun tearDown() {
    }
}