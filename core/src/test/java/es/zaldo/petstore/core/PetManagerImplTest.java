package es.zaldo.petstore.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.dao.PetDao;

/**
 * Tests for {@link PetManagerImpl} class.
 */
public class PetManagerImplTest {

    @Test
    public void testCreate() throws Exception {
        Pet pet = new Pet();
        PetDao mockedDao = Mockito.mock(PetDao.class);
        Mockito.when(mockedDao.upsert(pet)).thenReturn(pet);

        new PetManagerImpl(mockedDao).update(pet);

        Mockito.verify(mockedDao).upsert(pet);
    }

    @Test
    public void testLoadById() throws Exception {
        String id = "any_id";
        Pet pet = new Pet(id);
        PetDao mockedDao = Mockito.mock(PetDao.class);
        Mockito.when(mockedDao.loadById(id)).thenReturn(pet);

        new PetManagerImpl(mockedDao).loadById(id);

        Mockito.verify(mockedDao).loadById(id);
    }

    @Test(expected = NoSuchPetException.class)
    public void testLoadByIdNoSuchPet() throws Exception {
        String id = "any_id";
        PetDao mockedDao = Mockito.mock(PetDao.class);
        Mockito.when(mockedDao.loadById(id)).thenThrow(new NoSuchPetException(id));

        new PetManagerImpl(mockedDao).loadById(id);
    }

    @Test
    public void testUpdate() throws Exception {
        Pet pet = new Pet();
        PetDao mockedDao = Mockito.mock(PetDao.class);
        Mockito.when(mockedDao.upsert(pet)).thenReturn(pet);

        new PetManagerImpl(mockedDao).update(pet);

        Mockito.verify(mockedDao).upsert(pet);
    }

    @Test
    public void testGetPetsById() {
        List<String> ids = Arrays.asList("1", "2", "3");
        PetDao mockedDao = Mockito.mock(PetDao.class);
        Mockito.when(mockedDao.getPetsById(ids)).thenReturn(
                new Pets(new ArrayList<Pet>(), 0, 0, 0));

        new PetManagerImpl(mockedDao).getPetsById(ids);

        Mockito.verify(mockedDao).getPetsById(ids);
    }

    @Test
    public void testSearchWithin() {
        PetDao mockedDao = Mockito.mock(PetDao.class);
        Box bounds = new Box(new Location(0, 0), new Location(1, 1));
        PageRequest pagination = new PageRequest(0, 0);
        Mockito.when(mockedDao.searchWithin(null, null, bounds, pagination))
                .thenReturn(new Pets(new ArrayList<Pet>(), 0, 0, 0));

        new PetManagerImpl(mockedDao).searchWithin(null, null, bounds, pagination);

        Mockito.verify(mockedDao).searchWithin(null, null, bounds, pagination);
    }

}
