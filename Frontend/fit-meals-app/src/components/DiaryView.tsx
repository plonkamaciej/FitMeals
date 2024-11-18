import React, { useState, useEffect } from 'react';
import { Calendar } from './ui/calendar';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from './ui/card';
import { getSuggestions } from '../translations/foodTranslations';
import Settings from './Settings';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger, DialogDescription } from './ui/dialog';
import { Alert, AlertTitle, AlertDescription } from './ui/alert';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
 
interface Meal {
  name: string;
  protein: number;
  fat: number;
  carbs: number;
  calories: number;
  weight: number;
}
 
 
 
interface DailyMeals {
  breakfast: Meal[];
  lunch: Meal[];
  dinner: Meal[];
}
 
interface DailyEntry {
  date: string;
  meals: DailyMeals;
  totalCalories: number;
}
 
interface APIFood {
  id: string | null;
  name: string;
  calories: number;
  protein: number;
  fat: number;
  carbs: number;
  weight: number;
  meal: null;
}
 
interface MacroGoals {
  protein: number;
  carbs: number;
  fat: number;
}
 
interface DailyRequirements {
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
}
 
function MealForm({ addMeal }: { addMeal: (meal: Meal) => void }) {
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [selectedFood, setSelectedFood] = useState<APIFood | null>(null);
  const [weight, setWeight] = useState<number>(100);
 
  // Obsługa podpowiedzi
  useEffect(() => {
    if (searchQuery.length >= 2) {
      setSuggestions(getSuggestions(searchQuery));
    } else {
      setSuggestions([]);
    }
  }, [searchQuery]);
 
  const searchFoods = async (query: string) => {
    if (!query) return;
 
    try {
      // Wyciągnij angielską nazwę z formatu "nazwa_pl (nazwa_eng)"
      const englishName = query.match(/\((.*?)\)$/)?.[1] || query;
      const token = localStorage.getItem('token');
      if (!token) {
        console.error('Brak tokena JWT, nie można wykonać zapytania.');
        return;
      }
 
      const response = await fetch(`http://localhost:8081/api/foods/search/${englishName}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
 
      // Dodaj sprawdzenie statusu odpowiedzi
      if (!response.ok) {
        console.error(`Błąd serwera: ${response.status} - ${response.statusText}`);
        return;
      }
 
      const data = await response.json();
      if (data) {
        const foodWithId = {
          ...data,
          id: data.id || `temp-${Date.now()}`
        };
        setSelectedFood(foodWithId);
      } else {
        console.log("Nie znaleziono wyników dla zapytania:", englishName);
      }
    } catch (error) {
      console.error('Błąd podczas wyszukiwania:', error);
    }
  };
 
 
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (selectedFood) {
      const factor = weight / 100;
      const newMeal: Meal = {
        name: selectedFood.name,
        protein: Math.round(selectedFood.protein * factor * 10) / 10,
        fat: Math.round(selectedFood.fat * factor * 10) / 10,
        carbs: Math.round(selectedFood.carbs * factor * 10) / 10,
        calories: Math.round(selectedFood.calories * factor),
        weight: weight
      };
      addMeal(newMeal);
      setSelectedFood(null);
      setWeight(100);
      setSearchQuery('');
    }
  };
 
  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="relative">
        <Input
          type="text"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          placeholder="Wyszukaj posiłek..."
          className="w-full"
        />
 
        {/* Lista podpowiedzi */}
        {suggestions.length > 0 && (
          <div className="absolute z-10 w-full bg-white border rounded-md shadow-lg mt-1">
            {suggestions.map((suggestion, index) => (
              <div
                key={index}
                className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                onClick={() => {
                  setSearchQuery(suggestion);
                  setSuggestions([]);
                  searchFoods(suggestion);
                }}
              >
                {suggestion}
              </div>
            ))}
          </div>
        )}
      </div>
 
      <Input 
        type="number" 
        value={weight} 
        onChange={(e) => setWeight(Number(e.target.value))} 
        placeholder="Waga (g)" 
      />
 
      <Button 
        type="submit" 
        disabled={!selectedFood}
        className="w-full"
      >
        Dodaj posiłek
      </Button>
 
      {selectedFood && (
        <div className="mt-4 p-4 bg-gray-50 rounded-lg">
          <p className="font-medium">Wartości odżywcze na 100g:</p>
          <div className="space-y-1 mt-2">
            <p>Białko: {selectedFood.protein}g</p>
            <p>Tłuszcz: {selectedFood.fat}g</p>
            <p>Węglowodany: {selectedFood.carbs}g</p>
            <p>Kalorie: {selectedFood.calories}kcal</p>
          </div>
        </div>
      )}
    </form>
  );
}
 
function MealList({ meals, mealType, onDelete }: { 
  meals: Meal[], 
  mealType: keyof DailyMeals,
  onDelete: (mealType: keyof DailyMeals, index: number) => void 
}) {
  const totalNutrients = meals.reduce((acc, meal) => ({
    protein: acc.protein + meal.protein,
    fat: acc.fat + meal.fat,
    carbs: acc.carbs + meal.carbs,
    calories: acc.calories + meal.calories
  }), { protein: 0, fat: 0, carbs: 0, calories: 0 });
 
  return (
    <div className="space-y-4">
      <ul className="space-y-2">
        {meals.map((meal, index) => (
          <li key={index} className="flex items-center justify-between p-2 bg-gray-50 rounded-lg">
            <div className="flex-1">
              <span className="font-medium">{meal.name}</span>
              <div className="text-sm text-gray-600">
                Białko: {meal.protein}g, Tłuszcz: {meal.fat}g, Węglowodany: {meal.carbs}g, 
                Kalorie: {meal.calories}kcal, Waga: {meal.weight}g
              </div>
            </div>
            <Button
              variant="destructive"
              size="sm"
              onClick={() => onDelete(mealType, index)}
              className=" ml-10"
            >
              x
            </Button>
          </li>
        ))}
      </ul>
      <div className="text-sm font-medium">
        <strong>Suma:</strong> Białko: {Math.round(totalNutrients.protein * 10) / 10}g, 
        Tłuszcz: {Math.round(totalNutrients.fat * 10) / 10}g, 
        Węglowodany: {Math.round(totalNutrients.carbs * 10) / 10}g, 
        Kalorie: {Math.round(totalNutrients.calories)}kcal
      </div>
    </div>
  );
}
 
function MealSection({ 
  title, 
  meals, 
  addMeal, 
  mealType,
  onDelete 
}: { 
  title: string, 
  meals: Meal[], 
  addMeal: (meal: Meal) => void,
  mealType: keyof DailyMeals,
  onDelete: (mealType: keyof DailyMeals, index: number) => void
}) {
  return (
    <Card className="mb-4">
      <CardHeader>
        <CardTitle>{title}</CardTitle>
      </CardHeader>
      <CardContent>
        <MealForm addMeal={addMeal} />
      </CardContent>
      <CardFooter>
        <MealList 
          meals={meals} 
          mealType={mealType}
          onDelete={onDelete}
        />
      </CardFooter>
    </Card>
  );
}
 
function ActivityCalendar({ entries, caloryGoal, onDateSelect, selectedDate }: { 
  entries: DailyEntry[], 
  caloryGoal: number,
  onDateSelect: (date: Date) => void,
  selectedDate: string
}) {
  const getDayColor = (date: Date) => {
    const dateStr = date.toISOString().split('T')[0];
    const entry = entries.find(e => e.date === dateStr);
 
    const isSelected = dateStr === selectedDate;
 
    if (isSelected) {
      return 'bg-blue-300 font-bold';
    } else if (entry) {
      return entry.totalCalories <= caloryGoal ? 'bg-green-200' : 'bg-red-200';
    }
    return '';
  };
 
  return (
    <div className="flex justify-center">
      <Calendar 
        mode="single"
        onSelect={(date) => {
          if (date) {
            const localDate = new Date(date.setHours(12, 0, 0, 0));
            onDateSelect(localDate);
          }
        }}
        selected={new Date(selectedDate)}
        modifiers={{
          customColor: (date) => getDayColor(date) !== '',
        }}
        modifiersClassNames={{
          customColor: (date: Date) => getDayColor(date as string),
        }}
        className="border rounded-md p-4"
      />
    </div>
  );
}
 
function ProgressBar({ current, goal, label, color = "bg-blue-500", unit = "g" }: { 
  current: number; 
  goal: number; 
  label: string;
  color?: string;
  unit?: string;
}) {
  const percentage = Math.min((current / goal) * 100, 100);
 
  return (
    <div className="space-y-2">
      <div className="flex justify-between text-sm">
        <span>{label}</span>
        <span>{Math.round(current * 10) / 10} {unit} / {goal} {unit}</span>
      </div>
      <div className="h-2 w-full bg-gray-200 rounded-full overflow-hidden">
        <div 
          className={`h-full ${color} transition-all duration-300`} 
          style={{ width: `${percentage}%` }}
        />
      </div>
      <div className="text-right text-sm text-gray-500">
        {Math.round(percentage)}%
      </div>
    </div>
  );
}
 
function DiaryView() {
  const navigate = useNavigate();
  const [entries, setEntries] = useState<DailyEntry[]>([]);
  const [selectedDate, setSelectedDate] = useState<string>(new Date().toISOString().split('T')[0]);
  const [caloryGoal, setCaloryGoal] = useState<number>(0);
  const [macroGoals, setMacroGoals] = useState<MacroGoals>({
    protein: 0,
    carbs: 0,
    fat: 0
  });
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [hasRequirements, setHasRequirements] = useState(false);
 
  // Zaktualizowana funkcja fetchUserRequirements
  const fetchUserRequirements = async () => {
    try {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');
 
      if (!userId || !token) {
        console.error('Brak userId lub tokenu');
        setIsSettingsOpen(true);
        return;
      }
 
      const response = await axios.get<DailyRequirements>(
        `http://localhost:8081/api/users/${userId}/daily-requirements`,
        {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      );
 
      if (response.data) {
        console.log('Pobrane cele:', response.data);
        setCaloryGoal(response.data.calories);
        setMacroGoals({
          protein: response.data.protein,
          carbs: response.data.carbs,
          fat: response.data.fat
        });
        setHasRequirements(true);
      }
    } catch (error) {
      console.error('Błąd podczas pobierania celów:', error);
      setIsSettingsOpen(true);
      setHasRequirements(false);
    }
  };
 
  // Dodaj nowy useEffect do wywoływania fetchUserRequirements
  useEffect(() => {
    fetchUserRequirements();
  }, []); // Wywołaj tylko raz przy montowaniu komponentu
 
  useEffect(() => {
    const storedEntries = localStorage.getItem('diaryEntries');
    const storedGoal = localStorage.getItem('caloryGoal');
    const storedMacroGoals = localStorage.getItem('macroGoals');
 
    if (storedEntries) {
      try {
        const parsedEntries = JSON.parse(storedEntries);
        setEntries(parsedEntries);
      } catch (e) {
        console.error('Błąd podczas parsowania entries:', e);
        setEntries([]);
      }
    }
 
    if (storedGoal) {
      try {
        const parsedGoal = Number(storedGoal);
        if (!isNaN(parsedGoal)) {
          setCaloryGoal(parsedGoal);
        }
      } catch (e) {
        console.error('Błąd podczas parsowania caloryGoal:', e);
      }
    }
 
    if (storedMacroGoals) {
      try {
        const parsedMacroGoals = JSON.parse(storedMacroGoals);
        if (parsedMacroGoals && typeof parsedMacroGoals === 'object') {
          setMacroGoals(parsedMacroGoals);
        }
      } catch (e) {
        console.error('Błąd podczas parsowania macroGoals:', e);
      }
    }
  }, []);
 
  useEffect(() => {
    if (entries && entries.length >= 0) {
      localStorage.setItem('diaryEntries', JSON.stringify(entries));
    }
  }, [entries]);
 
  useEffect(() => {
    if (caloryGoal !== undefined && caloryGoal !== null) {
      localStorage.setItem('caloryGoal', caloryGoal.toString());
    }
  }, [caloryGoal]);
 
  useEffect(() => {
    if (macroGoals) {
      localStorage.setItem('macroGoals', JSON.stringify(macroGoals));
    }
  }, [macroGoals]);
 
  const getCurrentEntry = () => {
    return entries.find(entry => entry.date === selectedDate) || {
      date: selectedDate,
      meals: { breakfast: [], lunch: [], dinner: [] },
      totalCalories: 0
    };
  };
 
  const addMeal = async (mealType: keyof DailyMeals, meal: Meal) => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
 
    if (!userId || !token) {
      console.error('Brak userId lub tokenu');
      return;
    }
 
    // Pobierz aktualny wpis dla danego dnia
    const currentEntry = getCurrentEntry();
 
    // Przygotuj nową listę posiłków dla danego typu
    const updatedMealsList = [...currentEntry.meals[mealType], meal];
 
    // Przygotuj dane do wysłania
    const mealData = {
      mealType: mealType.toUpperCase(),
      foodList: updatedMealsList.map(meal => ({
        name: meal.name,
        calories: meal.calories,
        protein: meal.protein,
        fat: meal.fat,
        carbs: meal.carbs,
        weight: meal.weight
      }))
    };
 
    try {
      const response = await fetch(
        `http://localhost:8081/api/diary/${userId}/${selectedDate}`,
        {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(mealData)
        }
      );
 
      if (!response.ok) {
        throw new Error('Błąd podczas aktualizacji posiłku');
      }
 
      // Aktualizuj stan lokalny
      setEntries(prevEntries => {
        const updatedEntry = {
          ...currentEntry,
          meals: {
            ...currentEntry.meals,
            [mealType]: updatedMealsList
          },
          totalCalories: calculateTotalCalories(currentEntry.meals, mealType, updatedMealsList)
        };
        const newEntries = prevEntries.filter(entry => entry.date !== selectedDate);
        return [...newEntries, updatedEntry];
      });
 
    } catch (error) {
      console.error('Błąd podczas zapisywania posiłku:', error);
      // Tutaj możesz dodać wyświetlanie komunikatu o błędzie dla użytkownika
    }
  };
 
  // Pomocnicza funkcja do obliczania całkowitej liczby kalorii
  const calculateTotalCalories = (
    currentMeals: DailyMeals, 
    updatedMealType: keyof DailyMeals, 
    updatedMealsList: Meal[]
  ) => {
    const meals = {
      ...currentMeals,
      [updatedMealType]: updatedMealsList
    };
 
    return Object.values(meals).flat().reduce((sum, meal) => sum + meal.calories, 0);
  };
 
  const calculateDailyTotals = () => {
    const currentEntry = getCurrentEntry();
    const allMeals = [
      ...currentEntry.meals.breakfast,
      ...currentEntry.meals.lunch,
      ...currentEntry.meals.dinner
    ];
 
    return allMeals.reduce((acc, meal) => ({
      protein: acc.protein + meal.protein,
      fat: acc.fat + meal.fat,
      carbs: acc.carbs + meal.carbs,
      calories: acc.calories + meal.calories
    }), { protein: 0, fat: 0, carbs: 0, calories: 0 });
  };
 
  const currentEntry = getCurrentEntry();
  const dailyTotals = calculateDailyTotals();
 
  const handleDateSelect = (date: Date) => {
    const formattedDate = date.toISOString().split('T')[0];
    setSelectedDate(formattedDate);
    fetchDailyMeals(formattedDate);
  };
 
  useEffect(() => {
    console.log('Current goals:', { caloryGoal, macroGoals }); // debugging
  }, [caloryGoal, macroGoals]);
 
  // Dodaj nową funkcję do pobierania dziennika
  const fetchDailyMeals = async (date: string) => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
 
    if (!userId || !token) {
      console.error('Brak userId lub tokenu');
      return;
    }
 
    try {
      const response = await fetch(
        `http://localhost:8081/api/diary/${userId}/${date}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
 
      if (!response.ok) {
        throw new Error('Błąd podczas pobierania posiłków');
      }
 
      const data = await response.json();
 
      // Przygotuj nowy wpis do dziennika
      const newEntry: DailyEntry = {
        date: date,
        meals: {
          breakfast: data.breakfast?.foodList || [],
          lunch: data.lunch?.foodList || [],
          dinner: data.dinner?.foodList || []
        },
        totalCalories: calculateTotalCaloriesFromMeals({
          breakfast: data.breakfast?.foodList || [],
          lunch: data.lunch?.foodList || [],
          dinner: data.dinner?.foodList || []
        })
      };
 
      // Aktualizuj stan entries
      setEntries(prevEntries => {
        const newEntries = prevEntries.filter(entry => entry.date !== date);
        return [...newEntries, newEntry];
      });
 
    } catch (error) {
      console.error('Błąd podczas pobierania posiłków:', error);
    }
  };
 
  // Pomocnicza funkcja do obliczania kalorii
  const calculateTotalCaloriesFromMeals = (meals: DailyMeals) => {
    return Object.values(meals)
      .flat()
      .reduce((sum, meal) => sum + (meal?.calories || 0), 0);
  };
 
  // Dodaj useEffect do pobierania posiłków przy pierwszym renderowaniu
  useEffect(() => {
    const today = new Date().toISOString().split('T')[0];
    fetchDailyMeals(today);
  }, []); // Tylko przy pierwszym renderowaniu
 
  // Komponent karty z celami
  const GoalsCard = () => (
    <Card className="mb-4">
      <CardHeader>
        <CardTitle>Twoje cele</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div className="p-4 bg-gray-50 rounded-lg text-center">
            <p className="text-sm text-gray-600">Kalorie</p>
            <p className="text-xl font-bold">{caloryGoal} kcal</p>
          </div>
          <div className="p-4 bg-gray-50 rounded-lg text-center">
            <p className="text-sm text-gray-600">Białko</p>
            <p className="text-xl font-bold">{macroGoals.protein} g</p>
          </div>
          <div className="p-4 bg-gray-50 rounded-lg text-center">
            <p className="text-sm text-gray-600">Węglowodany</p>
            <p className="text-xl font-bold">{macroGoals.carbs} g</p>
          </div>
          <div className="p-4 bg-gray-50 rounded-lg text-center">
            <p className="text-sm text-gray-600">Tłuszcze</p>
            <p className="text-xl font-bold">{macroGoals.fat} g</p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
 
  // Dodaj nowe funkcje do obsługi edycji i usuwania
  const deleteMeal = async (mealType: keyof DailyMeals, mealIndex: number) => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
 
    if (!userId || !token) {
      console.error('Brak userId lub tokenu');
      return;
    }
 
    const currentEntry = getCurrentEntry();
    const updatedMealsList = currentEntry.meals[mealType].filter((_, index) => index !== mealIndex);
 
    const mealData = {
      mealType: mealType.toUpperCase(),
      foodList: updatedMealsList
    };
 
    try {
      const response = await fetch(
        `http://localhost:8081/api/diary/${userId}/${selectedDate}`,
        {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(mealData)
        }
      );
 
      if (!response.ok) {
        throw new Error('Błąd podczas usuwania posiłku');
      }
 
      setEntries(prevEntries => {
        const updatedEntry = {
          ...currentEntry,
          meals: {
            ...currentEntry.meals,
            [mealType]: updatedMealsList
          },
          totalCalories: calculateTotalCalories(currentEntry.meals, mealType, updatedMealsList)
        };
        const newEntries = prevEntries.filter(entry => entry.date !== selectedDate);
        return [...newEntries, updatedEntry];
      });
 
    } catch (error) {
      console.error('Błąd podczas usuwania posiłku:', error);
    }
  };
 
  const handleLogout = () => {
    // Wyczyść dane z localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('diaryEntries');
    localStorage.removeItem('caloryGoal');
    localStorage.removeItem('macroGoals');
 
    // Przekieruj do strony głównej
    navigate('/');
  };
 
  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-3xl font-bold">Dziennik Jedzenia</h1>
        <div className="flex gap-2">
          <Dialog open={isSettingsOpen} onOpenChange={setIsSettingsOpen}>
            <DialogTrigger asChild>
              <Button variant="outline">
                {hasRequirements ? 'Zmień cele' : 'Ustaw cele'}
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-2xl">
              <DialogHeader>
                <DialogTitle>
                  {hasRequirements ? 'Zmień swoje cele' : 'Ustaw swoje cele'}
                </DialogTitle>
                {!hasRequirements && (
                  <DialogDescription>
                    Nie masz jeszcze ustawionych celów. Wypełnij formularz, aby obliczyć swoje zapotrzebowanie.
                  </DialogDescription>
                )}
              </DialogHeader>
              <Settings 
                userId={Number(localStorage.getItem('userId'))} 
                onSettingsUpdate={(requirements) => {
                  setCaloryGoal(requirements.calories);
                  setMacroGoals({
                    protein: requirements.protein,
                    carbs: requirements.carbs,
                    fat: requirements.fat
                  });
                  setHasRequirements(true);
                  setIsSettingsOpen(false);
                }} 
              />
            </DialogContent>
          </Dialog>
          <Button 
            variant="destructive" 
            onClick={handleLogout}
          >
            Wyloguj
          </Button>
        </div>
      </div>
 
      {hasRequirements ? (
        <GoalsCard />
      ) : (
        <Alert className="mb-4">
          <AlertTitle>Brak ustawionych celów</AlertTitle>
          <AlertDescription>
            Kliknij przycisk "Ustaw cele" aby obliczyć swoje zapotrzebowanie kaloryczne i makroskładniki.
          </AlertDescription>
        </Alert>
      )}
 
      <Card className="mb-4">
        <CardHeader>
          <CardTitle>Kalendarz aktywności</CardTitle>
        </CardHeader>
        <CardContent>
          <ActivityCalendar 
            entries={entries} 
            caloryGoal={caloryGoal} 
            onDateSelect={handleDateSelect}
            selectedDate={selectedDate}
          />
          <div className="mt-4 text-center text-sm text-gray-600">
            Wybrana data: {new Date(selectedDate + 'T12:00:00').toLocaleDateString('pl-PL')}
          </div>
        </CardContent>
      </Card>
      <MealSection 
        title="Śniadanie" 
        meals={currentEntry.meals.breakfast} 
        addMeal={(meal) => addMeal('breakfast', meal)} 
        mealType="breakfast"
        onDelete={deleteMeal}
      />
      <MealSection 
        title="Obiad" 
        meals={currentEntry.meals.lunch} 
        addMeal={(meal) => addMeal('lunch', meal)} 
        mealType="lunch"
        onDelete={deleteMeal}
      />
      <MealSection 
        title="Kolacja" 
        meals={currentEntry.meals.dinner} 
        addMeal={(meal) => addMeal('dinner', meal)} 
        mealType="dinner"
        onDelete={deleteMeal}
      />
      <Card>
        <CardHeader>
          <CardTitle>Podsumowanie dnia</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-6">
            {/* Kalorie */}
            <ProgressBar
              current={dailyTotals.calories}
              goal={caloryGoal}
              label="Kalorie (kcal)"
              color="bg-purple-500"
              unit = "kcal"
            />
 
            {/* Białko */}
            <ProgressBar
              current={dailyTotals.protein}
              goal={macroGoals.protein}
              label="Białko"
              color="bg-red-500"
            />
 
            {/* Węglowodany */}
            <ProgressBar
              current={dailyTotals.carbs}
              goal={macroGoals.carbs}
              label="Węglowodany"
              color="bg-green-500"
            />
 
            {/* Tłuszcze */}
            <ProgressBar
              current={dailyTotals.fat}
              goal={macroGoals.fat}
              label="Tłuszcze"
              color="bg-yellow-500"
            />
 
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
 
export default DiaryView;